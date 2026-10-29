package com.example.engine

import android.content.Context
import android.content.SharedPreferences
import com.example.model.ChatMessage
import com.example.model.GameType
import com.example.model.LogType
import com.example.model.TerminalLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class CyberRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("cyber_vip_prefs", Context.MODE_PRIVATE)

    companion object {
        const val DEFAULT_TOKEN =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOiIxNzkyOTM4NTkyIiwibmJmIjoiMTc5MjkzODU5MiIsImV4cGlyYXRpb24iOiIxMC8yNS8yMDI2IDk6NTk6NTIgUE0iLCJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3JvbGUiOiJBY2Nlc3NfVG9rZW4iLCJVc2VySWQiOiIxMzkyNTIxIiwiVXNlclBhbWUiOiI6MjgzODYxNzMxODg1IiwiVXNlclBob3RvIjoiMTIiLCJOaWNrTmFtZSI6Ik1lbWJlck5OR1lERlZJIiwiQW1vdW50IjoiODMwLjAwIiwiSW50ZWdyYWwiOiIwIiwiTG9naW5NYXJrIjoiSDUiLCJMb2dpblRpbWUiOiIxMC8yNS8yMDI2IDk6Mjk6NTIgUE0iLCJMb2dpbklQQWRkcmVzcyI6IjEwMy4xNzIuNDIuMjM4IiwiRGJOdW1iZXIiOiIwIiwiSXN2YWxpZGF0b3IiOiIwIiwiS2V5Q29kZSI6IjIxMzAiLCJUb2tlblR5cGUiOiJBY2Nlc3NfVG9rZW4iLCJQaG9uZVR5cGUiOiIxIiwiVXNlclR5cGUiOiIwIiwiVXNlckBhbWUyIjoicml2YW5raGFpcnVsc2VuemFAZ21haWwuY29tIiwiaXNzIjoiand0SXNzdWVyIiwiYXVkIjoibG90dGVyeVRpY2tldCJ9.qmpRU2U9AkHFXkMOuYwvG5g6ikJzc6hhvkixi6_SETg"
        const val API_BASE = "https://api.55fiveapi.com/api/webapi"
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .build()

    private val _logs = MutableStateFlow<List<TerminalLog>>(emptyList())
    val logs: StateFlow<List<TerminalLog>> = _logs.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    init {
        addLog("VIP 5.5 Dual-Engine System Loaded.", LogType.SYS)
        loadInitialChat()
    }

    fun getLoggedUser(): String? {
        return prefs.getString("logged_user", null)
    }

    fun saveLogin(username: String) {
        prefs.edit()
            .putString("logged_user", username)
            .putLong("logged_time", System.currentTimeMillis())
            .apply()
        addLog("User '$username' authenticated into CYBER-X.", LogType.SUCCESS)
    }

    fun logout() {
        val user = getLoggedUser()
        prefs.edit().remove("logged_user").apply()
        addLog("User '$user' logged out.", LogType.SYS)
    }

    fun getApiToken(): String {
        return prefs.getString("api_token", DEFAULT_TOKEN) ?: DEFAULT_TOKEN
    }

    fun saveApiToken(token: String) {
        prefs.edit().putString("api_token", token.trim()).apply()
        addLog("API Token updated in secure cache.", LogType.SUCCESS)
    }

    fun isBypassMode(): Boolean {
        return prefs.getBoolean("bypass_mode", false)
    }

    fun setBypassMode(enabled: Boolean) {
        prefs.edit().putBoolean("bypass_mode", enabled).apply()
        if (enabled) {
            addLog("Local Prediction Engine & Offline Bypass ACTIVE.", LogType.WARN)
        } else {
            addLog("Direct 55Five API Connection ACTIVE.", LogType.SYS)
        }
    }

    fun addLog(msg: String, type: LogType = LogType.SYS) {
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val timeStr = timeFormat.format(Date())
        val newLog = TerminalLog(
            id = UUID.randomUUID().toString(),
            time = timeStr,
            message = msg,
            type = type
        )
        val current = _logs.value.toMutableList()
        current.add(newLog)
        if (current.size > 80) {
            current.removeAt(0)
        }
        _logs.value = current
    }

    private fun loadInitialChat() {
        val list = listOf(
            ChatMessage("1", "CyberHunter", "Seed signature match tembus 96.5%!", "15:20"),
            ChatMessage("2", "Member_992", "Gas Wingo 1-min min, lagi tren streak!", "15:21"),
            ChatMessage("3", "Vip_Matrix", "Sistem 2 Drift error rendah banget, akurat.", "15:22"),
            ChatMessage("4", "Anon_55", "Mode lokal bypass mantap buat latihan.", "15:23")
        )
        _chatMessages.value = list
    }

    fun sendChatMessage(text: String, sender: String) {
        if (text.isBlank()) return
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val timeStr = timeFormat.format(Date())
        val newMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            sender = sender,
            text = text.trim(),
            timeFormatted = timeStr,
            isMe = true
        )
        _chatMessages.value = _chatMessages.value + newMsg
    }

    suspend fun fetchIssue(gameType: GameType): String = withContext(Dispatchers.IO) {
        if (isBypassMode()) {
            return@withContext generateFallbackIssue()
        }

        val token = getApiToken()
        val authHeader = if (token.startsWith("Bearer ")) token else "Bearer $token"

        val jsonBody = JSONObject().apply {
            put("typeId", gameType.typeId)
            put("language", 1)
            put("random", "970465f2d1ca4742982826a945c7839e")
            put("signature", "5232F77555C8A357217CC8521D7D1B5A")
            put("timestamp", System.currentTimeMillis() / 1000)
        }

        try {
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val request = Request.Builder()
                .url("$API_BASE/GetGameIssue")
                .addHeader("Authorization", authHeader)
                .post(jsonBody.toString().toRequestBody(mediaType))
                .build()

            val response = client.newCall(request).execute()
            val bodyString = response.body?.string() ?: ""
            if (response.isSuccessful && bodyString.isNotBlank()) {
                val json = JSONObject(bodyString)
                if (json.optInt("code") == 0) {
                    val data = json.optJSONObject("data")
                    if (data != null) {
                        val predraw = data.optJSONObject("predraw")
                        val issue = predraw?.optString("issueNumber")
                            ?: data.optString("issueNumber")
                        if (!issue.isNullOrBlank()) {
                            return@withContext issue
                        }
                    }
                }
            }
        } catch (e: Exception) {
            // fallback
        }

        return@withContext generateFallbackIssue()
    }

    suspend fun fetchHistoryFeed(gameType: GameType): List<Int> = withContext(Dispatchers.IO) {
        if (isBypassMode()) {
            return@withContext List(15) { Random.nextInt(0, 10) }
        }

        val token = getApiToken()
        val authHeader = if (token.startsWith("Bearer ")) token else "Bearer $token"

        val jsonBody = JSONObject().apply {
            put("typeId", gameType.typeId)
            put("language", 1)
            put("pageSize", 15)
            put("pageNo", 1)
            put("random", "b422565265dd46b2bb9a6ccf82b3b8bc")
            put("signature", "EADB3378CB0A8CD3551CC67CE66091EC")
            put("timestamp", System.currentTimeMillis() / 1000)
        }

        try {
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val request = Request.Builder()
                .url("$API_BASE/GetNoaverageEmerdList")
                .addHeader("Authorization", authHeader)
                .post(jsonBody.toString().toRequestBody(mediaType))
                .build()

            val response = client.newCall(request).execute()
            val bodyString = response.body?.string() ?: ""
            if (response.isSuccessful && bodyString.isNotBlank()) {
                val json = JSONObject(bodyString)
                if (json.optInt("code") == 0) {
                    val data = json.opt("data")
                    val items: JSONArray? = when (data) {
                        is JSONArray -> data
                        is JSONObject -> data.optJSONArray("list")
                        else -> null
                    }
                    if (items != null && items.length() > 0) {
                        val result = mutableListOf<Int>()
                        for (i in 0 until items.length()) {
                            val item = items.optJSONObject(i)
                            if (item != null) {
                                val num = item.optInt("number", item.optInt("sumCount", 0))
                                result.add(num)
                            }
                        }
                        if (result.isNotEmpty()) return@withContext result
                    }
                }
            }
        } catch (e: Exception) {
            // fallback
        }

        return@withContext List(15) { Random.nextInt(0, 10) }
    }

    private fun generateFallbackIssue(): String {
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val dateStr = dateFormat.format(Date())
        val minuteOfDay = ((System.currentTimeMillis() / 60000) % 1440).toString().padStart(4, '0')
        return "$dateStr$minuteOfDay"
    }
}
