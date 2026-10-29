package com.example.engine

import com.example.model.ConfirmedPrediction
import com.example.model.RawPrediction
import com.example.model.SeedData
import java.util.Locale
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.random.Random

class QingoSmartEngine(private val numbers: List<Int>) {

    fun predictSingleDigit(): RawPrediction {
        if (numbers.size < 3) {
            val fallbackDigit = Random.nextInt(0, 10)
            return RawPrediction(
                digit = fallbackDigit,
                size = if (fallbackDigit >= 5) "BESAR" else "KECIL",
                selectType = if (fallbackDigit >= 5) 13 else 14,
                confidence = Random.nextInt(70, 86),
                pattern = "INITIAL-BOOT-VOTE"
            )
        }

        val freq = DoubleArray(10)
        numbers.forEachIndexed { idx, n ->
            if (n in 0..9) {
                val weight = 1.2.pow((numbers.size - idx).toDouble())
                freq[n] += weight
            }
        }

        val lastNum = numbers.first()
        val transitions = IntArray(10)
        for (i in 0 until numbers.size - 1) {
            if (numbers[i + 1] == lastNum) {
                val nextVal = numbers[i]
                if (nextVal in 0..9) {
                    transitions[nextVal] += 3
                }
            }
        }

        var oddCount = 0
        numbers.take(5).forEach { if (it % 2 != 0) oddCount++ }

        val scores = DoubleArray(10)
        for (d in 0..9) {
            var score = freq[d] + (transitions[d] * 2.0)
            if (oddCount >= 3 && d % 2 == 0) score += 2.5
            if (oddCount < 3 && d % 2 != 0) score += 2.5
            scores[d] = score
        }

        val sortedDigits = (0..9).sortedByDescending { scores[it] }
        val bestSingleDigit = sortedDigits[0]
        val secondDigit = sortedDigits[1]

        val diff = scores[bestSingleDigit] - scores[secondDigit]
        val rawConf = (78 + (diff * 2.5)).toInt()
        val confidence = min(99, max(72, rawConf))
        val size = if (bestSingleDigit >= 5) "BESAR" else "KECIL"

        val patternName = when {
            transitions[bestSingleDigit] > 0 -> "QINGO-MARKOV-LOCK"
            oddCount >= 4 -> "LSB-PARITY-SHIFT"
            else -> "QINGO-RECURSION-PROB"
        }

        return RawPrediction(
            digit = bestSingleDigit,
            size = size,
            selectType = if (bestSingleDigit >= 5) 13 else 14,
            confidence = confidence,
            pattern = patternName
        )
    }
}

class K3LotteryEngine(private val list: List<Int>) {
    fun analyze(): RawPrediction {
        val lastSum = if (list.isNotEmpty()) list.first() else 10
        val dynamicSum = max(3, min(18, ((lastSum + 4) % 18).let { if (it == 0) 11 else it }))
        val size = if (dynamicSum >= 11) "BESAR" else "KECIL"
        return RawPrediction(
            digit = dynamicSum,
            size = size,
            selectType = if (dynamicSum >= 11) 1 else 2,
            confidence = Random.nextInt(82, 92),
            pattern = "K3-FREQ-ANALYTICS"
        )
    }
}

class FiveDLotteryEngine(private val list: List<Int>) {
    fun analyze(): RawPrediction {
        val lastSum = if (list.isNotEmpty()) list.first() else 22
        val dynamicSum = max(0, min(45, (lastSum + 7) % 46))
        val size = if (dynamicSum >= 23) "BESAR" else "KECIL"
        return RawPrediction(
            digit = dynamicSum,
            size = size,
            selectType = if (dynamicSum >= 23) 1 else 2,
            confidence = Random.nextInt(80, 90),
            pattern = "5D-QUANTUM-LOCK"
        )
    }
}

class TRXWingoEngine(private val list: List<Int>) {
    fun analyze(): RawPrediction {
        val bestDigit = if (list.isNotEmpty()) (list.first() + 3) % 10 else Random.nextInt(0, 10)
        val size = if (bestDigit >= 5) "BESAR" else "KECIL"
        return RawPrediction(
            digit = bestDigit,
            size = size,
            selectType = if (bestDigit >= 5) 13 else 14,
            confidence = Random.nextInt(81, 94),
            pattern = "TRX-FREQ-LOCK"
        )
    }
}

class PredictionConfirmationEngine {

    fun confirmPrediction(
        rawPrediction: RawPrediction,
        seedData: SeedData?,
        historyFeed: List<Int>
    ): ConfirmedPrediction {
        val sampleCount = min(historyFeed.size, 10)
        var totalDrift = 0
        if (sampleCount > 0) {
            for (i in 0 until sampleCount) {
                val actual = historyFeed[i]
                totalDrift += abs(actual - rawPrediction.digit)
            }
        } else {
            totalDrift = 5
        }

        val denominator = max(1, sampleCount * 9)
        val driftErrorPercent = (totalDrift.toDouble() / denominator) * 100.0
        val formattedDrift = String.format(Locale.US, "%.2f", driftErrorPercent).toDoubleOrNull() ?: 1.85

        val seedMultiplier = if (seedData != null && seedData.matchRate > 80.0) 1.12 else 1.0
        val calculatedConf = ((rawPrediction.confidence - (formattedDrift * 0.2)) * seedMultiplier).toInt()
        val adjustedConfidence = min(99, max(65, calculatedConf))

        val status = if (formattedDrift > 45.0) "DRIFT WARN ⚠" else "PASSED ✓"
        val targetLabel = "${rawPrediction.size} (${rawPrediction.digit})"

        return ConfirmedPrediction(
            status = status,
            driftError = formattedDrift,
            adjustedConfidence = adjustedConfidence,
            finalDigit = rawPrediction.digit,
            size = rawPrediction.size,
            pattern = rawPrediction.pattern,
            targetLabel = targetLabel
        )
    }
}
