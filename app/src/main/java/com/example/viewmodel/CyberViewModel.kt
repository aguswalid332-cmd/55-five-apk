package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.engine.CyberRepository
import com.example.engine.FiveDLotteryEngine
import com.example.engine.K3LotteryEngine
import com.example.engine.PredictionConfirmationEngine
import com.example.engine.QingoSmartEngine
import com.example.engine.SeedDiscoveryEngine
import com.example.engine.TRXWingoEngine
import com.example.model.ChatMessage
import com.example.model.ConfirmedPrediction
import com.example.model.GameType
import com.example.model.LogType
import com.example.model.RawPrediction
import com.example.model.SeedData
import com.example.model.TerminalLog
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

sealed class ScreenState {
    object AUTH : ScreenState()
    object WELCOME : ScreenState()
    object PREDICTOR : ScreenState()
}

class CyberViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CyberRepository(application)
    private val seedEngine = SeedDiscoveryEngine()
    private val confirmEngine = PredictionConfirmationEngine()

    private val _screen = MutableStateFlow<ScreenState>(ScreenState.AUTH)
    val screen: StateFlow<ScreenState> = _screen.asStateFlow()

    private val _currentUser = MutableStateFlow<String?>(null)
    val currentUser: StateFlow<String?> = _currentUser.asStateFlow()

    private val _activeGame = MutableStateFlow(GameType.WINGO)
    val activeGame: StateFlow<GameType> = _activeGame.asStateFlow()

    private val _currentIssue = MutableStateFlow("SCANNING...")
    val currentIssue: StateFlow<String> = _currentIssue.asStateFlow()

    private val _seedData = MutableStateFlow(
        SeedData("WAITING DATA...", 0.0, "")
    )
    val seedData: StateFlow<SeedData> = _seedData.asStateFlow()

    private val _isSeedCracking = MutableStateFlow(false)
    val isSeedCracking: StateFlow<Boolean> = _isSeedCracking.asStateFlow()

    private val _prediction = MutableStateFlow<ConfirmedPrediction?>(null)
    val prediction: StateFlow<ConfirmedPrediction?> = _prediction.asStateFlow()

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    private val _showTokenConfig = MutableStateFlow(false)
    val showTokenConfig: StateFlow<Boolean> = _showTokenConfig.asStateFlow()

    private val _tokenInput = MutableStateFlow(repository.getApiToken())
    val tokenInput: StateFlow<String> = _tokenInput.asStateFlow()

    private val _isBypassMode = MutableStateFlow(repository.isBypassMode())
    val isBypassMode: StateFlow<Boolean> = _isBypassMode.asStateFlow()

    private val _showChat = MutableStateFlow(false)
    val showChat: StateFlow<Boolean> = _showChat.asStateFlow()

    val logs: StateFlow<List<TerminalLog>> = repository.logs
    val chatMessages: StateFlow<List<ChatMessage>> = repository.chatMessages

    private var pollJob: Job? = null
    private var lastObservedIssue = ""

    init {
        val savedUser = repository.getLoggedUser()
        if (!savedUser.isNullOrBlank()) {
            _currentUser.value = savedUser
            _screen.value = ScreenState.WELCOME
        }
    }

    fun login(username: String) {
        val trimmed = username.trim()
        if (trimmed.isNotBlank()) {
            repository.saveLogin(trimmed)
            _currentUser.value = trimmed
            _screen.value = ScreenState.WELCOME
        }
    }

    fun logout() {
        stopPolling()
        repository.logout()
        _currentUser.value = null
        _screen.value = ScreenState.AUTH
    }

    fun proceedToPredictor() {
        _screen.value = ScreenState.PREDICTOR
        startPolling()
    }

    fun selectGame(game: GameType) {
        _activeGame.value = game
        lastObservedIssue = ""
        repository.addLog("Game switched to ${game.displayName}", LogType.SYS)
        runEngineCycle()
    }

    fun toggleTokenConfig(show: Boolean? = null) {
        _showTokenConfig.value = show ?: !_showTokenConfig.value
    }

    fun updateTokenInput(token: String) {
        _tokenInput.value = token
    }

    fun saveToken() {
        repository.saveApiToken(_tokenInput.value)
        _showTokenConfig.value = false
        runEngineCycle()
    }

    fun enableBypassMode() {
        repository.setBypassMode(true)
        _isBypassMode.value = true
        _showTokenConfig.value = false
        runEngineCycle()
    }

    fun disableBypassMode() {
        repository.setBypassMode(false)
        _isBypassMode.value = false
        runEngineCycle()
    }

    fun toggleChat(show: Boolean? = null) {
        _showChat.value = show ?: !_showChat.value
    }

    fun sendChat(text: String) {
        val user = _currentUser.value ?: "GUEST"
        repository.sendChatMessage(text, user)
    }

    fun triggerManualSeedCrack() {
        viewModelScope.launch {
            _isSeedCracking.value = true
            repository.addLog("Triggering Signature Break algorithm...", LogType.SEED)
            delay(600)
            val cracked = seedEngine.crackSeedFromSignature(typeId = _activeGame.value.typeId)
            _seedData.value = cracked
            _isSeedCracking.value = false
            repository.addLog(
                "[MANUAL_SEED] Hash Match Rate: ${cracked.matchRate}% -> Locked ${cracked.seed}",
                LogType.SEED
            )
            runEngineCycle(force = true)
        }
    }

    private fun startPolling() {
        stopPolling()
        pollJob = viewModelScope.launch {
            while (isActive) {
                runEngineCycle()
                delay(3500)
            }
        }
    }

    private fun stopPolling() {
        pollJob?.cancel()
        pollJob = null
    }

    private fun runEngineCycle(force: Boolean = false) {
        viewModelScope.launch {
            _isAnalyzing.value = true
            val game = _activeGame.value
            val issue = repository.fetchIssue(game)
            val history = repository.fetchHistoryFeed(game)

            val seed = seedEngine.crackSeedFromSignature(typeId = game.typeId)
            _seedData.value = seed

            if (force || issue != lastObservedIssue) {
                lastObservedIssue = issue
                _currentIssue.value = issue

                val rawPred: RawPrediction = when (game) {
                    GameType.WINGO -> QingoSmartEngine(history).predictSingleDigit()
                    GameType.K3 -> K3LotteryEngine(history).analyze()
                    GameType.FIVE_D -> FiveDLotteryEngine(history).analyze()
                    GameType.TRX -> TRXWingoEngine(history).analyze()
                }

                val confirmed = confirmEngine.confirmPrediction(rawPred, seed, history)
                _prediction.value = confirmed

                repository.addLog(
                    "[SEED-1] Match: ${seed.matchRate}% -> Seed ${seed.seed}",
                    LogType.SEED
                )
                repository.addLog(
                    "[CONFIRM-2] Issue ${issue.takeLast(6)} Validated -> ${confirmed.size} (${confirmed.finalDigit}) [${confirmed.status}]",
                    LogType.CONFIRM
                )
            }
            _isAnalyzing.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopPolling()
    }
}
