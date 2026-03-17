package com.poojan.waygo_driver.ui.profile

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poojan.waygo_driver.ui.theme.*

@Composable
fun DriverProfileScreen(
    initialDriverName   : String = "Ravi Kumar",
    initialPhone        : String = "+91 98765 43210",
    initialEmail        : String = "ravikumar@email.com",
    initialCity         : String = "Ahmedabad, Gujarat",
    initialVehicle      : String = "Maruti Swift Dzire (White)",
    initialRegNo        : String = "GJ 01 MF 7890",
    totalTrips   : String = "1,248",
    weeklyEarning: String = "₹8,420",
    acceptance   : String = "92%",
    rating       : Float  = 4.9f,
    onBackClick         : () -> Unit = {},
    onEarningsClick     : () -> Unit = {},
    onSettingsClick     : () -> Unit = {},
    onSupportClick      : () -> Unit = {},
    onDocumentsClick    : () -> Unit = {},
    onVehicleClick      : () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onLogoutClick       : () -> Unit = {}
) {
    val colors = LocalWayGoColors.current

    var showLogoutDialog by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(false) }

    var driverName by remember { mutableStateOf(initialDriverName) }
    var phone by remember { mutableStateOf(initialPhone) }
    var email by remember { mutableStateOf(initialEmail) }
    var city by remember { mutableStateOf(initialCity) }
    var vehicle by remember { mutableStateOf(initialVehicle) }
    var regNo by remember { mutableStateOf(initialRegNo) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        // ── Gradient Header ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(
                    Brush.linearGradient(
                        if (colors.isLight)
                            listOf(Color(0xFFFFF8E1), Color(0xFFFFF3C4), colors.background)
                        else
                            listOf(Color(0xFF1A1400), Color(0xFF2A2000), colors.background)
                    )
                )
        ) {
            // Decorative circle
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .offset(x = (-30).dp, y = (-30).dp)
                    .clip(CircleShape)
                    .background(YellowPrimary.copy(alpha = 0.06f))
            )

            // Back button
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(12.dp)
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(colors.surface.copy(alpha = 0.8f))
            ) {
                Icon(Icons.Default.ArrowBack, null, tint = colors.textPrimary)
            }

            // Save Button
            if (isEditing) {
                Button(
                    onClick = { isEditing = false },
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(12.dp)
                        .align(Alignment.TopEnd)
                        .height(36.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = YellowPrimary),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    Text("Save", color = BgDark, fontWeight = FontWeight.Bold)
                }
            }

            // Profile Info
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(84.dp)
                            .shadow(12.dp, CircleShape)
                            .clip(CircleShape)
                            .background(colors.surfaceElevated)
                            .border(3.dp, YellowPrimary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("😊", fontSize = 38.sp)
                    }
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(YellowPrimary)
                            .clickable { isEditing = !isEditing },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isEditing) Icons.Default.Close else Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = BgDark,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(Modifier.width(14.dp))

                Column {
                    if (isEditing) {
                        OutlinedTextField(
                            value = driverName,
                            onValueChange = { driverName = it },
                            textStyle = LocalTextStyle.current.copy(color = colors.textPrimary, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = YellowPrimary,
                                unfocusedBorderColor = colors.divider,
                                cursorColor = YellowPrimary
                            ),
                            modifier = Modifier.height(54.dp).width(200.dp)
                        )
                    } else {
                        Text(driverName, color = colors.textPrimary, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 3.dp)) {
                            repeat(5) { index ->
                                Text(if (index < rating.toInt()) "★" else "☆", color = YellowPrimary, fontSize = 14.sp)
                            }
                            Spacer(Modifier.width(6.dp))
                            Text(rating.toString(), color = YellowPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.height(5.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(YellowPrimary)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text("⭐ TOP DRIVER", color = BgDark, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 0.5.sp)
                        }
                    }
                }
            }
        }

        // ── Scrollable Content ──
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ProfileStatCard(totalTrips, "Total Trips", Modifier.weight(1f))
                ProfileStatCard(weeklyEarning, "This Week", Modifier.weight(1f))
                ProfileStatCard(acceptance, "Acceptance", Modifier.weight(1f))
            }

            Spacer(Modifier.height(16.dp))

            // ── Verification Steps ──
            AnimatedVisibility(visible = !isEditing) {
                Column {
                    Text("VERIFICATION STATUS", color = YellowPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp, modifier = Modifier.padding(bottom = 10.dp))

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.surface)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            VerificationStep("Aadhaar Card", "Verified", true)
                            VerificationStep("Driving License", "Verified", true)
                            VerificationStep("Vehicle RC", "Under Review", false)
                            VerificationStep("Profile Photo", "Verified", true)
                        }
                    }

                    Spacer(Modifier.height(14.dp))
                }
            }

            // Personal Details
            Text("PERSONAL DETAILS", color = YellowPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp, modifier = Modifier.padding(bottom = 10.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface)
            ) {
                Column {
                    ProfileInfoRow(Icons.Default.Phone, "Phone", phone, isEditing, { phone = it }, KeyboardType.Phone, true)
                    ProfileInfoRow(Icons.Default.Email, "Email", email, isEditing, { email = it }, KeyboardType.Email, true)
                    ProfileInfoRow(Icons.Default.LocationOn, "City", city, isEditing, { city = it }, KeyboardType.Text, false)
                }
            }

            Spacer(Modifier.height(14.dp))

            // Vehicle Details
            Text("VEHICLE DETAILS", color = YellowPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp, modifier = Modifier.padding(bottom = 10.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface)
            ) {
                Column {
                    ProfileInfoRow(Icons.Default.DirectionsCar, "Vehicle", vehicle, isEditing, { vehicle = it }, KeyboardType.Text, true)
                    ProfileInfoRow(Icons.Default.Badge, "Registration No.", regNo, isEditing, { regNo = it }, KeyboardType.Text, false)
                }
            }

            Spacer(Modifier.height(14.dp))

            // Menu Options
            AnimatedVisibility(visible = !isEditing) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.surface)
                ) {
                    Column {
                        MenuRow(Icons.Default.AccountBalanceWallet, "Earnings History", false, onEarningsClick)
                        HorizontalDivider(color = colors.divider, modifier = Modifier.padding(horizontal = 16.dp))
                        MenuRow(Icons.Default.Description, "Documents", false, onDocumentsClick)
                        HorizontalDivider(color = colors.divider, modifier = Modifier.padding(horizontal = 16.dp))
                        MenuRow(Icons.Default.DirectionsCar, "Vehicle Details", false, onVehicleClick)
                        HorizontalDivider(color = colors.divider, modifier = Modifier.padding(horizontal = 16.dp))
                        MenuRow(Icons.Default.Notifications, "Notifications", false, onNotificationsClick)
                        HorizontalDivider(color = colors.divider, modifier = Modifier.padding(horizontal = 16.dp))
                        MenuRow(Icons.Default.Headset, "Support & Help", false, onSupportClick)
                        HorizontalDivider(color = colors.divider, modifier = Modifier.padding(horizontal = 16.dp))
                        MenuRow(Icons.Default.Settings, "Settings", false, onSettingsClick)
                        HorizontalDivider(color = colors.divider, modifier = Modifier.padding(horizontal = 16.dp))
                        MenuRow(Icons.Default.Logout, "Logout", true) { showLogoutDialog = true }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }

    // Logout Dialog
    if (showLogoutDialog) {
        LogoutDialog(
            onConfirm = {
                showLogoutDialog = false
                onLogoutClick()
            },
            onDismiss = { showLogoutDialog = false }
        )
    }
}

@Composable
fun VerificationStep(name: String, status: String, isVerified: Boolean) {
    val colors = LocalWayGoColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(if (isVerified) GreenAccent.copy(alpha = 0.15f) else OrangeAccent.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                if (isVerified) Icons.Default.CheckCircle else Icons.Default.HourglassTop,
                null,
                tint = if (isVerified) GreenAccent else OrangeAccent,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(Modifier.width(12.dp))
        Text(name, color = colors.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(if (isVerified) GreenAccent.copy(alpha = 0.12f) else OrangeAccent.copy(alpha = 0.12f))
                .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
            Text(
                status,
                color = if (isVerified) GreenAccent else OrangeAccent,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ProfileStatCard(value: String, label: String, modifier: Modifier = Modifier) {
    val colors = LocalWayGoColors.current
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface)
    ) {
        Column(
            modifier = Modifier.padding(14.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, color = YellowPrimary, fontSize = 18.sp, fontWeight = FontWeight.Black)
            Text(label, color = colors.textSecondary, fontSize = 10.sp, modifier = Modifier.padding(top = 2.dp))
        }
    }
}

@Composable
fun ProfileInfoRow(
    icon: ImageVector,
    label: String,
    value: String,
    isEditing: Boolean,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType,
    showDivider: Boolean
) {
    val colors = LocalWayGoColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = if (isEditing) 6.dp else 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = YellowPrimary, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(12.dp))

        Column(Modifier.weight(1f)) {
            Text(label, color = colors.textSecondary, fontSize = 10.sp)
            if (isEditing) {
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(color = colors.textPrimary, fontSize = 13.sp),
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = ImeAction.Next),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = YellowPrimary,
                        unfocusedBorderColor = colors.divider,
                        cursorColor = YellowPrimary
                    ),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                )
            } else {
                Text(value, color = colors.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        if (!isEditing) {
            Icon(Icons.Default.Edit, null, tint = colors.textSecondary.copy(alpha = 0.5f), modifier = Modifier.size(16.dp))
        }
    }
    if (showDivider) HorizontalDivider(color = colors.divider, modifier = Modifier.padding(horizontal = 16.dp))
}

@Composable
fun MenuRow(icon: ImageVector, label: String, isRed: Boolean, onClick: () -> Unit) {
    val colors = LocalWayGoColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = if (isRed) RedAccent else YellowPrimary, modifier = Modifier.size(22.dp))
        Spacer(Modifier.width(14.dp))
        Text(
            label,
            color = if (isRed) RedAccent else colors.textPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        if (!isRed) Icon(Icons.Default.ChevronRight, null, tint = colors.textSecondary, modifier = Modifier.size(20.dp))
    }
}

@Composable
fun LogoutDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    val colors = LocalWayGoColors.current
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = colors.surface,
        titleContentColor = RedAccent,
        textContentColor = colors.textSecondary,
        title = { Text("Log Out", fontWeight = FontWeight.Bold) },
        text = { Text("Are you sure you want to securely log out of your driver account?") },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = RedAccent)
            ) {
                Text("Log Out", fontWeight = FontWeight.Bold, color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = colors.textPrimary)
            }
        }
    )
}