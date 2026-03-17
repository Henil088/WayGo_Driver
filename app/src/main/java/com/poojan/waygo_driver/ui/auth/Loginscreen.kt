package com.poojan.waygo_driver.ui.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poojan.waygo_driver.ui.theme.*

@Composable
fun LoginScreen(
    onLoginClick: (phone: String, password: String) -> Unit = { _, _ -> },
    onSignUpClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    isLoading: Boolean = false
) {
    val colors = LocalWayGoColors.current
    var phone    by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var phoneError    by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        // Yellow Arc Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(
                    brush = Brush.verticalGradient(colors = listOf(YellowPrimary, YellowDark)),
                    shape = RoundedCornerShape(bottomStart = 60.dp, bottomEnd = 60.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 40.dp)
            ) {
                AnimatedVisibility(
                    visible = visible,
                    enter = scaleIn(spring(Spring.DampingRatioMediumBouncy)) + fadeIn()
                ) {
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(BgDark),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🚗", fontSize = 30.sp)
                    }
                }

                Spacer(Modifier.height(10.dp))

                AnimatedVisibility(
                    visible = visible,
                    enter = slideInVertically { -40 } + fadeIn(tween(400, delayMillis = 150))
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("WAYGO", fontSize = 34.sp, fontWeight = FontWeight.Black, color = BgDark, letterSpacing = 4.sp)
                        Text("DRIVER PARTNER", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = BgDark.copy(alpha = 0.6f), letterSpacing = 2.sp)
                    }
                }
            }
        }

        // Login Card
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(tween(500, delayMillis = 300)) { 80 } + fadeIn(tween(500, delayMillis = 300)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 220.dp)
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
            ) {
                Column(modifier = Modifier.padding(28.dp)) {
                    Text("Welcome Back", color = colors.textPrimary, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                    Text("Sign in to continue driving", color = colors.textSecondary, fontSize = 12.sp, modifier = Modifier.padding(top = 3.dp, bottom = 22.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it; phoneError = "" },
                        label = { Text("Mobile Number or Email") },
                        leadingIcon = { Icon(Icons.Default.Phone, null, tint = YellowPrimary, modifier = Modifier.size(20.dp)) },
                        isError = phoneError.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        colors = wayGoTextFieldColors()
                    )
                    if (phoneError.isNotEmpty()) {
                        Text(phoneError, color = RedAccent, fontSize = 11.sp, modifier = Modifier.padding(start = 14.dp, top = 3.dp))
                    }

                    Spacer(Modifier.height(14.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it; passwordError = "" },
                        label = { Text("Password") },
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = YellowPrimary, modifier = Modifier.size(20.dp)) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    null, tint = colors.textSecondary
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        isError = passwordError.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = wayGoTextFieldColors()
                    )
                    if (passwordError.isNotEmpty()) {
                        Text(passwordError, color = RedAccent, fontSize = 11.sp, modifier = Modifier.padding(start = 14.dp, top = 3.dp))
                    }

                    Text(
                        text = "Forgot Password?",
                        color = YellowPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.align(Alignment.End).padding(top = 8.dp).clickable { onForgotPasswordClick() }
                    )

                    Spacer(Modifier.height(20.dp))

                    Button(
                        onClick = {
                            var valid = true
                            if (phone.isBlank()) { phoneError = "Enter mobile number or email"; valid = false }
                            if (password.isBlank()) { passwordError = "Enter your password"; valid = false }
                            if (valid) onLoginClick(phone, password)
                        },
                        enabled = !isLoading,
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = YellowPrimary, contentColor = BgDark),
                        elevation = ButtonDefaults.buttonElevation(8.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(Modifier.size(20.dp), color = BgDark, strokeWidth = 2.dp)
                        } else {
                            Text("SIGN IN", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, letterSpacing = 1.sp)
                        }
                    }

                    Row(
                        modifier = Modifier.padding(vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(Modifier.weight(1f), color = colors.divider)
                        Text("  OR  ", color = colors.textSecondary, fontSize = 11.sp)
                        HorizontalDivider(Modifier.weight(1f), color = colors.divider)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("New driver? ", color = colors.textSecondary, fontSize = 12.sp)
                        Text(
                            text = "Register Now",
                            color = YellowPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { onSignUpClick() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun wayGoTextFieldColors(): TextFieldColors {
    val colors = LocalWayGoColors.current
    return OutlinedTextFieldDefaults.colors(
        focusedBorderColor = YellowPrimary,
        unfocusedBorderColor = colors.divider,
        focusedLabelColor = YellowPrimary,
        unfocusedLabelColor = colors.textSecondary,
        cursorColor = YellowPrimary,
        focusedTextColor = colors.textPrimary,
        unfocusedTextColor = colors.textPrimary,
        errorBorderColor = RedAccent,
        unfocusedContainerColor = colors.surfaceElevated,
        focusedContainerColor = colors.surfaceElevated
    )
}