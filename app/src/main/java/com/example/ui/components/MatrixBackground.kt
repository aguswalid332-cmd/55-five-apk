package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.ui.theme.CyberNeonCyan
import com.example.ui.theme.CyberNeonGreen
import kotlin.random.Random

@Composable
fun MatrixBackground(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "matrix_anim")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    // Generate stable column drop offsets
    val columns = 16
    val dropSpeeds = remember { FloatArray(columns) { Random.nextFloat() * 0.6f + 0.4f } }
    val dropOffsets = remember { FloatArray(columns) { Random.nextFloat() } }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val colWidth = width / columns

        // Draw faint scanlines
        var yScan = 0f
        while (yScan < height) {
            drawLine(
                color = Color(0x0A00F0FF),
                start = Offset(0f, yScan),
                end = Offset(width, yScan),
                strokeWidth = 1f
            )
            yScan += 4f
        }

        // Draw digital matrix stream points
        for (i in 0 until columns) {
            val speed = dropSpeeds[i]
            val initial = dropOffsets[i]
            val progress = (initial + phase * speed) % 1.0f
            val x = i * colWidth + (colWidth / 2f)
            val headY = progress * (height + 200f) - 100f

            for (trail in 0..7) {
                val pointY = headY - (trail * 22f)
                if (pointY in 0f..height) {
                    val alpha = (1.0f - (trail / 8f)) * 0.18f
                    val dotColor = if (trail == 0) {
                        CyberNeonCyan.copy(alpha = alpha * 1.5f)
                    } else {
                        CyberNeonGreen.copy(alpha = alpha)
                    }
                    drawCircle(
                        color = dotColor,
                        radius = if (trail == 0) 2.5f else 1.8f,
                        center = Offset(x, pointY)
                    )
                }
            }
        }
    }
}
