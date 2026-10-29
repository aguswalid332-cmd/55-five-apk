package com.example

import com.example.engine.PredictionConfirmationEngine
import com.example.engine.QingoSmartEngine
import com.example.engine.SeedDiscoveryEngine
import com.example.model.RawPrediction
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testSeedDiscoveryEngine() {
        val engine = SeedDiscoveryEngine()
        val seedData = engine.crackSeedFromSignature(
            signature = "5232F77555C8A357217CC8521D7D1B5A",
            randomStr = "970465f2d1ca4742982826a945c7839e",
            timestamp = 1792938592L,
            typeId = 1
        )
        assertNotNull(seedData.seed)
        assertTrue(seedData.seed.startsWith("0x"))
        assertTrue(seedData.matchRate in 50.0..100.0)
    }

    @Test
    fun testQingoSmartEnginePrediction() {
        val numbers = listOf(7, 3, 2, 8, 9, 1, 4, 6, 7, 5)
        val engine = QingoSmartEngine(numbers)
        val prediction = engine.predictSingleDigit()
        assertTrue(prediction.digit in 0..9)
        assertTrue(prediction.size == "BESAR" || prediction.size == "KECIL")
        assertTrue(prediction.confidence in 50..100)
    }

    @Test
    fun testPredictionConfirmationEngine() {
        val confirmEngine = PredictionConfirmationEngine()
        val raw = RawPrediction(digit = 7, size = "BESAR", selectType = 13, confidence = 85, pattern = "MARKOV")
        val numbers = listOf(7, 8, 6, 9)
        val confirmed = confirmEngine.confirmPrediction(raw, null, numbers)
        assertEquals(7, confirmed.finalDigit)
        assertEquals("BESAR", confirmed.size)
        assertTrue(confirmed.adjustedConfidence > 0)
    }
}
