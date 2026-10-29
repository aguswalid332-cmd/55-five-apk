package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val CyberColorScheme = darkColorScheme(
    primary = CyberNeonCyan,
    onPrimary = CyberBlack,
    primaryContainer = CyberPanel,
    onPrimaryContainer = CyberNeonCyan,
    secondary = CyberNeonGreen,
    onSecondary = CyberBlack,
    secondaryContainer = CyberPanelLight,
    onSecondaryContainer = CyberNeonGreen,
    tertiary = CyberNeonPink,
    onTertiary = CyberWhite,
    background = CyberBlack,
    onBackground = CyberNeonCyan,
    surface = CyberDark,
    onSurface = CyberWhite,
    surfaceVariant = CyberPanel,
    onSurfaceVariant = CyberTextDim,
    outline = CyberNeonCyan
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CyberColorScheme,
        typography = Typography,
        content = content
    )
}
