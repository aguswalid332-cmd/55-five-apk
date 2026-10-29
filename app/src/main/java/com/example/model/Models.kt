package com.example.model

enum class GameType(val code: String, val displayName: String, val typeId: Int) {
    WINGO("WINGO", "WINGO 1-MINUTE (TYPE 1)", 1),
    K3("K3", "K3 LOTRE 1-MINUTE (TYPE 9)", 9),
    FIVE_D("5D", "5D LOTRE 1-MINUTE (TYPE 5)", 5),
    TRX("TRX", "TRX WINGO 1-MINUTE (TYPE 13)", 13)
}

data class SeedData(
    val seed: String,
    val matchRate: Double,
    val calculatedHash: String
)

data class RawPrediction(
    val digit: Int,
    val size: String,
    val selectType: Int,
    val confidence: Int,
    val pattern: String
)

data class ConfirmedPrediction(
    val status: String, // "PASSED ✓" or "DRIFT WARN ⚠"
    val driftError: Double,
    val adjustedConfidence: Int,
    val finalDigit: Int,
    val size: String,
    val pattern: String,
    val targetLabel: String
)

data class ChatMessage(
    val id: String,
    val sender: String,
    val text: String,
    val timeFormatted: String,
    val isMe: Boolean = false
)

enum class LogType {
    SYS, SEED, CONFIRM, WARN, SUCCESS
}

data class TerminalLog(
    val id: String,
    val time: String,
    val message: String,
    val type: LogType
)
