package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.KeyOff
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameType
import com.example.model.LogType
import com.example.model.TerminalLog
import com.example.ui.components.CyberCard
import com.example.ui.components.RadarSweep
import com.example.ui.components.StatusBadge
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberBorderPink
import com.example.ui.theme.CyberBorderYellow
import com.example.ui.theme.CyberDark
import com.example.ui.theme.CyberDim
import com.example.ui.theme.CyberNeonCyan
import com.example.ui.theme.CyberNeonGreen
import com.example.ui.theme.CyberNeonPink
import com.example.ui.theme.CyberNeonYellow
import com.example.ui.theme.CyberPanel
import com.example.ui.theme.CyberTextDim
import com.example.ui.theme.CyberWhite
import com.example.viewmodel.CyberViewModel

@Composable
fun PredictorScreen(
    viewModel: CyberViewModel,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val activeGame by viewModel.activeGame.collectAsState()
    val currentIssue by viewModel.currentIssue.collectAsState()
    val seedData by viewModel.seedData.collectAsState()
    val isSeedCracking by viewModel.isSeedCracking.collectAsState()
    val prediction by viewModel.prediction.collectAsState()
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
    val showTokenConfig by viewModel.showTokenConfig.collectAsState()
    val tokenInput by viewModel.tokenInput.collectAsState()
    val isBypassMode by viewModel.isBypassMode.collectAsState()
    val showChat by viewModel.showChat.collectAsState()
    val logs by viewModel.logs.collectAsState()
    val chatMessages by viewModel.chatMessages.collectAsState()

    val scrollState = rememberScrollState()

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // 1. TOP HUD BAR
            CyberCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = CyberBorder,
                backgroundColor = CyberPanel.copy(alpha = 0.92f),
                cornerRadius = 10.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(CyberNeonCyan.copy(alpha = 0.2f))
                                .border(1.dp, CyberNeonCyan, RoundedCornerShape(6.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Memory,
                                contentDescription = "CPU HUD",
                                tint = CyberNeonCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "CYBER-X VIP 5.5",
                                color = CyberWhite,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            )
                            Text(
                                text = "USER: ${(currentUser ?: "GUEST").uppercase()}",
                                color = CyberNeonGreen,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 9.sp
                                )
                            )
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedButton(
                            onClick = { viewModel.toggleTokenConfig() },
                            modifier = Modifier
                                .height(32.dp)
                                .testTag("config_button"),
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = CyberNeonYellow.copy(alpha = 0.15f),
                                contentColor = CyberNeonYellow
                            ),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = androidx.compose.ui.graphics.SolidColor(CyberBorderYellow)
                            )
                        ) {
                            Text(
                                text = "🔑 CONFIG",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp
                                )
                            )
                        }

                        OutlinedButton(
                            onClick = { viewModel.logout() },
                            modifier = Modifier
                                .height(32.dp)
                                .testTag("logout_button"),
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = CyberNeonPink.copy(alpha = 0.15f),
                                contentColor = CyberNeonPink
                            ),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = androidx.compose.ui.graphics.SolidColor(CyberBorderPink)
                            )
                        ) {
                            Text(
                                text = "LOGOUT",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp
                                )
                            )
                        }
                    }
                }
            }

            // 2. MANUAL TOKEN CONFIG PANEL (COLLAPSIBLE)
            AnimatedVisibility(
                visible = showTokenConfig,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                CyberCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = CyberNeonYellow,
                    backgroundColor = CyberBlack.copy(alpha = 0.95f),
                    cornerRadius = 10.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Key,
                                    contentDescription = null,
                                    tint = CyberNeonYellow,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "CONFIG MANUAL TOKEN & DIRECT API",
                                    color = CyberNeonYellow,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                            IconButton(
                                onClick = { viewModel.toggleTokenConfig(false) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close Config",
                                    tint = CyberDim,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Text(
                            text = "BEARER TOKEN (Paste JWT Token):",
                            color = CyberDim,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp)
                        )

                        OutlinedTextField(
                            value = tokenInput,
                            onValueChange = { viewModel.updateTokenInput(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(72.dp)
                                .testTag("bearer_token_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberNeonYellow,
                                unfocusedBorderColor = CyberBorder,
                                focusedTextColor = CyberNeonGreen,
                                unfocusedTextColor = CyberNeonGreen,
                                focusedContainerColor = CyberDark,
                                unfocusedContainerColor = CyberDark
                            ),
                            textStyle = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.sp
                            )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { viewModel.saveToken() },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(36.dp)
                                    .testTag("save_token_button"),
                                shape = RoundedCornerShape(6.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = CyberNeonYellow.copy(alpha = 0.2f),
                                    contentColor = CyberNeonYellow
                                ),
                                border = ButtonDefaults.outlinedButtonBorder.copy(
                                    brush = androidx.compose.ui.graphics.SolidColor(CyberNeonYellow)
                                )
                            ) {
                                Text(
                                    text = "SIMPAN TOKEN",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                )
                            }

                            OutlinedButton(
                                onClick = {
                                    if (isBypassMode) viewModel.disableBypassMode()
                                    else viewModel.enableBypassMode()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(36.dp)
                                    .testTag("bypass_mode_button"),
                                shape = RoundedCornerShape(6.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (isBypassMode) CyberNeonGreen.copy(alpha = 0.2f) else CyberNeonCyan.copy(alpha = 0.2f),
                                    contentColor = if (isBypassMode) CyberNeonGreen else CyberNeonCyan
                                ),
                                border = ButtonDefaults.outlinedButtonBorder.copy(
                                    brush = androidx.compose.ui.graphics.SolidColor(
                                        if (isBypassMode) CyberNeonGreen else CyberNeonCyan
                                    )
                                )
                            ) {
                                Text(
                                    text = if (isBypassMode) "MODE ONLINE" else "MODE LOKAL",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // 3. GAME SELECTOR
            CyberCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = CyberBorder,
                backgroundColor = CyberDark.copy(alpha = 0.9f),
                cornerRadius = 10.dp
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SportsEsports,
                            contentDescription = null,
                            tint = CyberNeonCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "SELECT GAME:",
                            color = CyberNeonCyan,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        )
                    }

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth().testTag("game_selector_row")
                    ) {
                        items(GameType.values()) { game ->
                            val isSelected = game == activeGame
                            val bg = if (isSelected) CyberNeonGreen.copy(alpha = 0.18f) else CyberPanel
                            val border = if (isSelected) CyberNeonGreen else CyberBorder
                            val textCol = if (isSelected) CyberNeonGreen else CyberTextDim

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(bg)
                                    .border(1.dp, border, RoundedCornerShape(6.dp))
                                    .clickable { viewModel.selectGame(game) }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                                    .testTag("game_chip_${game.code}")
                            ) {
                                Text(
                                    text = game.displayName,
                                    color = textCol,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // 4. SISTEM 1: SEED DISCOVERY ENGINE UI PANEL
            CyberCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = CyberBorderPink,
                backgroundColor = CyberDark.copy(alpha = 0.95f),
                cornerRadius = 10.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Key,
                                contentDescription = null,
                                tint = CyberNeonPink,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "SISTEM 1: SEED SIGNATURE DISCOVERY",
                                color = CyberNeonPink,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            )
                        }

                        Text(
                            text = if (isSeedCracking) "CRACKING MD5..." else "READY TO REVERSE",
                            color = CyberNeonYellow,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.sp
                            )
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "RECONSTRUCTED SEED:",
                                color = CyberDim,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp)
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(CyberBlack)
                                    .border(1.dp, CyberBorder, RoundedCornerShape(4.dp))
                                    .padding(6.dp)
                            ) {
                                Text(
                                    text = seedData.seed,
                                    color = CyberNeonGreen,
                                    maxLines = 1,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp
                                    )
                                )
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "HASH MATCH RATE:",
                                color = CyberDim,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp)
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(CyberBlack)
                                    .border(1.dp, CyberBorder, RoundedCornerShape(4.dp))
                                    .padding(6.dp)
                            ) {
                                Text(
                                    text = "${seedData.matchRate}%",
                                    color = CyberNeonCyan,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp
                                    )
                                )
                            }
                        }
                    }

                    OutlinedButton(
                        onClick = { viewModel.triggerManualSeedCrack() },
                        enabled = !isSeedCracking,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(38.dp)
                            .testTag("trigger_seed_crack_button"),
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = CyberNeonPink.copy(alpha = 0.2f),
                            contentColor = CyberNeonPink
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = androidx.compose.ui.graphics.SolidColor(CyberNeonPink)
                        )
                    ) {
                        if (isSeedCracking) {
                            CircularProgressIndicator(
                                color = CyberNeonPink,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                        Text(
                            text = "⚡ RUN SEED REVERSE ENGINE (SIGNATURE BREAK)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }

            // 5. PREDICTION LOCK BOX
            CyberCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = CyberNeonCyan.copy(alpha = 0.6f),
                backgroundColor = CyberPanel.copy(alpha = 0.95f),
                cornerRadius = 12.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(0.dp, CyberBorder)
                            .padding(bottom = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.TrackChanges,
                                contentDescription = null,
                                tint = CyberNeonPink,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "55FIVE ${activeGame.code} SIGNAL LOCK",
                                color = CyberWhite,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            )
                        }

                        Text(
                            text = "ISSUE: $currentIssue",
                            color = CyberNeonCyan,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.sp
                            )
                        )
                    }

                    // Center Radar & Giant Digit
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        RadarSweep(
                            modifier = Modifier.size(140.dp),
                            color = CyberNeonCyan
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "TARGET RESULT PREDICTION",
                                color = CyberDim,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 8.sp,
                                    letterSpacing = 1.sp
                                )
                            )

                            val digitText = prediction?.finalDigit?.toString() ?: "?"
                            Text(
                                text = digitText,
                                color = CyberNeonGreen,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 58.sp
                                )
                            )

                            val classText = prediction?.let { "${it.size} (SINGLE ${it.finalDigit})" } ?: "ANALYZING..."
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(CyberNeonCyan.copy(alpha = 0.12f))
                                    .border(1.dp, CyberNeonCyan.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                                    .padding(horizontal = 10.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = classText,
                                    color = CyberNeonCyan,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }
                    }

                    // SISTEM 2: PREDICTION CONFIRMATION STATS
                    CyberCard(
                        modifier = Modifier.fillMaxWidth(),
                        borderColor = CyberBorder,
                        backgroundColor = CyberDark.copy(alpha = 0.92f),
                        cornerRadius = 8.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Security,
                                        contentDescription = null,
                                        tint = CyberNeonGreen,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "SISTEM 2: PREDICTION CONFIRMATION",
                                        color = CyberDim,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.sp
                                        )
                                    )
                                }

                                val statusBadgeText = prediction?.status ?: "VERIFYING"
                                val isPassed = statusBadgeText.contains("PASSED")
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(
                                            if (isPassed) CyberNeonGreen.copy(alpha = 0.18f)
                                            else CyberNeonPink.copy(alpha = 0.18f)
                                        )
                                        .border(
                                            1.dp,
                                            if (isPassed) CyberNeonGreen else CyberNeonPink,
                                            RoundedCornerShape(4.dp)
                                        )
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = statusBadgeText,
                                        color = if (isPassed) CyberNeonGreen else CyberNeonPink,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 8.sp
                                        )
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                val driftStr = prediction?.let { "${it.driftError}%" } ?: "0.00%"
                                Text(
                                    text = "DRIFT ERROR: $driftStr",
                                    color = CyberWhite,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp)
                                )

                                val confStr = prediction?.let { "${it.adjustedConfidence}%" } ?: "0%"
                                Text(
                                    text = "CONFIDENCE: $confStr",
                                    color = CyberNeonYellow,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp
                                    )
                                )
                            }

                            // Confidence progress bar
                            val confValue = (prediction?.adjustedConfidence ?: 0) / 100f
                            val animatedConf by animateFloatAsState(
                                targetValue = confValue,
                                animationSpec = tween(500),
                                label = "conf_anim"
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(5.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(CyberBlack)
                                    .border(0.5.dp, CyberBorder, RoundedCornerShape(4.dp))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(animatedConf)
                                        .height(5.dp)
                                        .background(
                                            Brush.horizontalGradient(
                                                listOf(CyberNeonCyan, CyberNeonGreen)
                                            )
                                        )
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                val pattern = prediction?.pattern ?: "SCANNING..."
                                Text(
                                    text = "PATTERN: $pattern",
                                    color = CyberDim,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp)
                                )

                                val target = prediction?.targetLabel ?: "BESAR (13)"
                                Text(
                                    text = "TARGET: $target",
                                    color = CyberNeonGreen,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 8.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // 6. TERMINAL ENGINE LOGS
            CyberCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                borderColor = CyberBorder,
                backgroundColor = CyberBlack,
                cornerRadius = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(0.dp, CyberBorder)
                            .padding(bottom = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Terminal,
                                contentDescription = null,
                                tint = CyberNeonCyan,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "TERMINAL ENGINE",
                                color = CyberNeonCyan,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp
                                )
                            )
                        }

                        Text(
                            text = if (isAnalyzing) "ANALYZING" else "IDLE",
                            color = if (isAnalyzing) CyberNeonYellow else CyberNeonGreen,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 8.sp
                            )
                        )
                    }

                    val logListState = rememberLazyListState()
                    LaunchedEffect(logs.size) {
                        if (logs.isNotEmpty()) {
                            logListState.animateScrollToItem(logs.size - 1)
                        }
                    }

                    LazyColumn(
                        state = logListState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(logs, key = { it.id }) { log ->
                            TerminalLogRow(log = log)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(60.dp))
        }

        // Floating Action Button for Community Chat
        FloatingActionButton(
            onClick = { viewModel.toggleChat(true) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(18.dp)
                .testTag("open_chat_button"),
            containerColor = CyberNeonCyan,
            contentColor = CyberBlack,
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Default.Message,
                contentDescription = "Live Chat",
                modifier = Modifier.size(24.dp)
            )
        }

        // Chat Dialog
        if (showChat) {
            ChatDialog(
                messages = chatMessages,
                onSendMessage = { viewModel.sendChat(it) },
                onDismiss = { viewModel.toggleChat(false) }
            )
        }
    }
}

@Composable
fun TerminalLogRow(log: TerminalLog) {
    val color = when (log.type) {
        LogType.SYS -> CyberNeonCyan
        LogType.SEED -> CyberNeonPink
        LogType.CONFIRM -> CyberNeonGreen
        LogType.WARN -> CyberNeonYellow
        LogType.SUCCESS -> CyberNeonGreen
    }

    Text(
        text = "[${log.time}] ${log.message}",
        color = color,
        style = MaterialTheme.typography.bodyMedium.copy(
            fontFamily = FontFamily.Monospace,
            fontSize = 8.5.sp,
            lineHeight = 12.sp
        ),
        modifier = Modifier.padding(vertical = 1.dp)
    )
}
