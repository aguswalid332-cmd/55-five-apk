package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CyberCard
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberDim
import com.example.ui.theme.CyberNeonBlue
import com.example.ui.theme.CyberNeonCyan
import com.example.ui.theme.CyberNeonGreen
import com.example.ui.theme.CyberNeonPink
import com.example.ui.theme.CyberPanel
import com.example.ui.theme.CyberWhite

@Composable
fun AuthScreen(
    onLoginSuccess: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isRegisterMode by remember { mutableStateOf(true) }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CyberCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            borderColor = CyberNeonBlue.copy(alpha = 0.5f),
            backgroundColor = CyberPanel.copy(alpha = 0.95f),
            cornerRadius = 16.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Shield icon
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(CyberBlack)
                        .border(2.dp, CyberNeonCyan, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "Shield Security",
                        tint = CyberNeonCyan,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "LOGIN BUAT AKUN",
                    color = CyberNeonCyan,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.5.sp
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Tabs: Belum Buat Akun / Sudah Punya Akun
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CyberBorder, RoundedCornerShape(8.dp))
                        .padding(2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                if (isRegisterMode) CyberNeonGreen.copy(alpha = 0.15f)
                                else CyberBlack.copy(alpha = 0.3f)
                            )
                            .clickable {
                                isRegisterMode = true
                                errorMessage = ""
                            }
                            .padding(vertical = 10.dp)
                            .testTag("register_tab_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "BELUM BUAT AKUN",
                            color = if (isRegisterMode) CyberNeonGreen else CyberDim,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                if (!isRegisterMode) CyberNeonCyan.copy(alpha = 0.15f)
                                else CyberBlack.copy(alpha = 0.3f)
                            )
                            .clickable {
                                isRegisterMode = false
                                errorMessage = ""
                            }
                            .padding(vertical = 10.dp)
                            .testTag("login_tab_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "SUDAH PUNYA AKUN",
                            color = if (!isRegisterMode) CyberNeonCyan else CyberDim,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Form fields
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "USERNAME",
                        color = CyberDim,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp)
                    )
                    OutlinedTextField(
                        value = username,
                        onValueChange = {
                            username = it
                            errorMessage = ""
                        },
                        placeholder = { Text("Masukkan Username", color = CyberDim, fontSize = 12.sp) },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null, tint = CyberNeonCyan, modifier = Modifier.size(18.dp))
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberNeonCyan,
                            unfocusedBorderColor = CyberBorder,
                            focusedTextColor = CyberWhite,
                            unfocusedTextColor = CyberWhite,
                            focusedContainerColor = CyberBlack,
                            unfocusedContainerColor = CyberBlack
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("username_input")
                    )

                    Text(
                        text = "PASSWORD",
                        color = CyberDim,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp)
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            errorMessage = ""
                        },
                        placeholder = { Text("Masukkan Password", color = CyberDim, fontSize = 12.sp) },
                        visualTransformation = PasswordVisualTransformation(),
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = CyberNeonCyan, modifier = Modifier.size(18.dp))
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberNeonCyan,
                            unfocusedBorderColor = CyberBorder,
                            focusedTextColor = CyberWhite,
                            unfocusedTextColor = CyberWhite,
                            focusedContainerColor = CyberBlack,
                            unfocusedContainerColor = CyberBlack
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("password_input")
                    )

                    if (isRegisterMode) {
                        Text(
                            text = "KONFIRMASI PASSWORD",
                            color = CyberDim,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp)
                        )
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = {
                                confirmPassword = it
                                errorMessage = ""
                            },
                            placeholder = { Text("Ulangi Password", color = CyberDim, fontSize = 12.sp) },
                            visualTransformation = PasswordVisualTransformation(),
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = CyberNeonGreen, modifier = Modifier.size(18.dp))
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = {
                                handleAuthSubmit(isRegisterMode, username, password, confirmPassword, { errorMessage = it }, onLoginSuccess)
                            }),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberNeonGreen,
                                unfocusedBorderColor = CyberBorder,
                                focusedTextColor = CyberWhite,
                                unfocusedTextColor = CyberWhite,
                                focusedContainerColor = CyberBlack,
                                unfocusedContainerColor = CyberBlack
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("confirm_password_input")
                        )
                    }
                }

                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage,
                        color = CyberNeonPink,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Submit Button
                val buttonColor = if (isRegisterMode) CyberNeonGreen else CyberNeonCyan
                OutlinedButton(
                    onClick = {
                        handleAuthSubmit(
                            isRegisterMode,
                            username,
                            password,
                            confirmPassword,
                            { errorMessage = it },
                            onLoginSuccess
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("auth_submit_button"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = buttonColor.copy(alpha = 0.2f),
                        contentColor = buttonColor
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(buttonColor)
                    )
                ) {
                    Text(
                        text = if (isRegisterMode) "BUAT AKUN SEKARANG" else "LOGIN AKUN",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp
                        )
                    )
                }
            }
        }
    }
}

private fun handleAuthSubmit(
    isRegister: Boolean,
    u: String,
    p: String,
    c: String,
    onError: (String) -> Unit,
    onSuccess: (String) -> Unit
) {
    val trimmedU = u.trim()
    val trimmedP = p.trim()
    if (trimmedU.isEmpty()) {
        onError("Masukkan username terlebih dahulu!")
        return
    }
    if (trimmedP.isEmpty()) {
        onError("Masukkan password!")
        return
    }
    if (isRegister) {
        if (trimmedP != c.trim()) {
            onError("Konfirmasi password tidak cocok!")
            return
        }
    }
    onSuccess(trimmedU)
}
