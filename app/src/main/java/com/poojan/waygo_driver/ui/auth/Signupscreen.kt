package com.poojan.waygo_driver.ui.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poojan.waygo_driver.ui.theme.*

@Composable
fun SignupScreen(
    onBackClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    val colors = LocalWayGoColors.current
    var fullName  by remember { mutableStateOf("") }
    var phone     by remember { mutableStateOf("") }
    var email     by remember { mutableStateOf("") }
    var password  by remember { mutableStateOf("") }
    var city      by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var termsAccepted   by remember { mutableStateOf(false) }
    val currentStep = 1

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(colors.surface)
            ) {
                Icon(Icons.Default.ArrowBack, null, tint = colors.textPrimary)
            }
            Spacer(Modifier.width(12.dp))
            Text("Create Account", color = colors.textPrimary, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
        }

        // Step Indicator
        StepIndicator(currentStep = currentStep)

        // Scrollable Form
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            // Profile Photo
            Column(
                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(88.dp)
                            .clip(CircleShape)
                            .background(colors.surfaceElevated)
                            .border(3.dp, YellowPrimary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("👤", fontSize = 38.sp)
                    }
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(YellowPrimary)
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.CameraAlt, null, tint = BgDark, modifier = Modifier.size(14.dp))
                    }
                }
                Text("Upload profile photo", color = colors.textSecondary, fontSize = 11.sp, modifier = Modifier.padding(top = 8.dp))
            }

            Text("PERSONAL INFORMATION", color = YellowPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp, modifier = Modifier.padding(bottom = 12.dp))

            OutlinedTextField(
                value = fullName, onValueChange = { fullName = it },
                label = { Text("Full Name") },
                leadingIcon = { Icon(Icons.Default.Person, null, tint = YellowPrimary, modifier = Modifier.size(20.dp)) },
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = wayGoTextFieldColors()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = phone, onValueChange = { phone = it },
                label = { Text("Mobile Number") },
                leadingIcon = { Icon(Icons.Default.Phone, null, tint = YellowPrimary, modifier = Modifier.size(20.dp)) },
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), colors = wayGoTextFieldColors()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = email, onValueChange = { email = it },
                label = { Text("Email Address") },
                leadingIcon = { Icon(Icons.Default.Email, null, tint = YellowPrimary, modifier = Modifier.size(20.dp)) },
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), colors = wayGoTextFieldColors()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = password, onValueChange = { password = it },
                label = { Text("Create Password") },
                leadingIcon = { Icon(Icons.Default.Lock, null, tint = YellowPrimary, modifier = Modifier.size(20.dp)) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, null, tint = colors.textSecondary)
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = wayGoTextFieldColors()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = city, onValueChange = { city = it },
                label = { Text("City") },
                leadingIcon = { Icon(Icons.Default.LocationOn, null, tint = YellowPrimary, modifier = Modifier.size(20.dp)) },
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = wayGoTextFieldColors()
            )
            Spacer(Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = termsAccepted,
                    onCheckedChange = { termsAccepted = it },
                    colors = CheckboxDefaults.colors(checkedColor = YellowPrimary, checkmarkColor = BgDark)
                )
                Text("I agree to the ", color = colors.textSecondary, fontSize = 12.sp)
                Text("Terms & Conditions", color = YellowPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { })
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onNextClick,
                enabled = termsAccepted,
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = YellowPrimary, contentColor = BgDark, disabledContainerColor = YellowPrimary.copy(alpha = 0.4f)),
                elevation = ButtonDefaults.buttonElevation(8.dp)
            ) {
                Text("NEXT", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, letterSpacing = 1.sp)
                Spacer(Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = BgDark, modifier = Modifier.size(18.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Already registered? ", color = colors.textSecondary, fontSize = 12.sp)
                Text("Sign In", color = YellowPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onLoginClick() })
            }
        }
    }
}

@Composable
fun StepIndicator(currentStep: Int) {
    val colors = LocalWayGoColors.current
    val steps = listOf("Personal", "Vehicle", "Documents")
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        steps.forEachIndexed { index, label ->
            val stepNum = index + 1
            val isActive = stepNum == currentStep
            val isDone = stepNum < currentStep

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(if (isActive || isDone) YellowPrimary else colors.surfaceElevated)
                        .border(
                            width = if (!isActive && !isDone) 1.5.dp else 0.dp,
                            color = if (!isActive && !isDone) colors.divider else Color.Transparent,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isDone) "✓" else stepNum.toString(),
                        color = if (isActive || isDone) BgDark else colors.textSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    label,
                    color = if (isActive) YellowPrimary else colors.textSecondary,
                    fontSize = 9.sp,
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
                )
            }

            if (index < steps.size - 1) {
                Box(
                    modifier = Modifier
                        .width(36.dp)
                        .height(2.dp)
                        .padding(bottom = 18.dp)
                        .background(if (stepNum < currentStep) YellowPrimary else colors.divider)
                )
            }
        }
    }
}