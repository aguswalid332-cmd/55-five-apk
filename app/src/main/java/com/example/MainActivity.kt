package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.components.MatrixBackground
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.PredictorScreen
import com.example.ui.screens.WelcomeScreen
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.CyberViewModel
import com.example.viewmodel.ScreenState

class MainActivity : ComponentActivity() {

    private val viewModel: CyberViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainApp(viewModel: CyberViewModel) {
    val currentScreen by viewModel.screen.collectAsState()

    // Back handling for screens
    BackHandler(enabled = currentScreen != ScreenState.AUTH) {
        when (currentScreen) {
            ScreenState.PREDICTOR -> viewModel.logout()
            ScreenState.WELCOME -> viewModel.logout()
            else -> {}
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        containerColor = CyberBlack
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CyberBlack)
                .padding(innerPadding)
        ) {
            // Live Digital Canvas Matrix Rain & Scanlines
            MatrixBackground()

            // Screen Switcher
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                label = "screen_transition"
            ) { screen ->
                when (screen) {
                    ScreenState.AUTH -> {
                        AuthScreen(
                            onLoginSuccess = { user ->
                                viewModel.login(user)
                            }
                        )
                    }

                    ScreenState.WELCOME -> {
                        WelcomeScreen(
                            onActivate = {
                                viewModel.proceedToPredictor()
                            }
                        )
                    }

                    ScreenState.PREDICTOR -> {
                        PredictorScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}
