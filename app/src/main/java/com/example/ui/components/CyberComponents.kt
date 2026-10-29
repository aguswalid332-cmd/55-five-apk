package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberDark
import com.example.ui.theme.CyberNeonCyan
import com.example.ui.theme.CyberNeonGreen
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CyberCard(
    modifier: Modifier = Modifier,
    borderColor: Color = CyberBorder,
    backgroundColor: Color = CyberDark.copy(alpha = 0.94f),
    cornerRadius: Dp = 12.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor, shape)
            .border(1.dp, borderColor, shape),
        content = content
    )
}

@Composable
fun RadarSweep(
    modifier: Modifier = Modifier,
    color: Color = CyberNeonCyan
) {
    val infiniteTransition = rememberInfiniteTransition(label = "radar_sweep")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "angle"
    )

    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val radius = size.minDimension / 2f

        // Concentric circles
        drawCircle(
            color = color.copy(alpha = 0.15f),
            radius = radius * 0.33f,
            center = center,
            style = Stroke(width = 1f)
        )
        drawCircle(
            color = color.copy(alpha = 0.2f),
            radius = radius * 0.66f,
            center = center,
            style = Stroke(width = 1f)
        )
        drawCircle(
            color = color.copy(alpha = 0.25f),
            radius = radius,
            center = center,
            style = Stroke(width = 1.2f)
        )

        // Crosshairs
        drawLine(
            color = color.copy(alpha = 0.1f),
            start = Offset(center.x - radius, center.y),
            end = Offset(center.x + radius, center.y),
            strokeWidth = 1f
        )
        drawLine(
            color = color.copy(alpha = 0.1f),
            start = Offset(center.x, center.y - radius),
            end = Offset(center.x, center.y + radius),
            strokeWidth = 1f
        )

        // Sweep line
        val rad = Math.toRadians(angle.toDouble())
        val lineEnd = Offset(
            (center.x + radius * cos(rad)).toFloat(),
            (center.y + radius * sin(rad)).toFloat()
        )
        drawLine(
            brush = Brush.radialGradient(
                colors = listOf(color.copy(alpha = 0.6f), color.copy(alpha = 0.05f)),
                center = center,
                radius = radius
            ),
            start = center,
            end = lineEnd,
            strokeWidth = 2f
        )
    }
}

@Composable
fun StatusBadge(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = CyberNeonGreen
) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = modifier
            .clip(shape)
            .background(color.copy(alpha = 0.12f), shape)
            .border(1.dp, color.copy(alpha = 0.6f), shape)
            .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.labelSmall.copy(
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 9.sp
            )
        )
    }
}
