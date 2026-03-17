package com.poojan.waygo_driver.ui.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.LatLng
import com.poojan.waygo_driver.ui.theme.*
import com.poojan.waygo_driver.ui.trip.TripRequestSheet
import com.poojan.waygo_driver.ui.auth.wayGoTextFieldColors
import kotlinx.coroutines.delay
import java.util.Locale

enum class RideState {
    IDLE,
    REQUESTED,
    EN_ROUTE_PICKUP,
    ARRIVED_PICKUP,
    IN_TRIP,
    PAYMENT_COLLECTION
}

// Ahmedabad mock locations
val DRIVER_LOC = LatLng(23.0225, 72.5714)       // Ahmedabad center
val PICKUP_LOC = LatLng(23.0350, 72.5560)        // Navrangpura
val DROP_LOC   = LatLng(23.0120, 72.5100)        // Satellite

@Composable
fun HomeScreen(
    driverName: String      = "Ravi Kumar",
    todayEarning: String    = "₹1,240",
    tripCount: String       = "8",
    rating: String          = "4.9",
    onProfileClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {}
) {
    val colors = LocalWayGoColors.current

    var rideState       by remember { mutableStateOf(RideState.IDLE) }
    var isOnline        by remember { mutableStateOf(false) }
    var buttonScale     by remember { mutableFloatStateOf(1f) }
    val animatedScale   by animateFloatAsState(buttonScale, spring(Spring.DampingRatioMediumBouncy), label = "btnScale")

    // Earnings count-up
    var displayEarning by remember { mutableStateOf("₹0") }
    LaunchedEffect(Unit) {
        val target = 1240
        for (i in 0..target step 40) {
            displayEarning = "₹${String.format(Locale.US, "%,d", i)}"
            delay(16)
        }
        displayEarning = "₹1,240"
    }

    // Mock ride request
    LaunchedEffect(isOnline, rideState) {
        if (isOnline && rideState == RideState.IDLE) {
            delay(3000)
            rideState = RideState.REQUESTED
        }
    }

    // Determine map state
    val showRoute = rideState in listOf(RideState.EN_ROUTE_PICKUP, RideState.IN_TRIP, RideState.ARRIVED_PICKUP)
    val pickupForMap = if (rideState != RideState.IDLE && rideState != RideState.REQUESTED) PICKUP_LOC else null
    val dropForMap = if (rideState == RideState.IN_TRIP || rideState == RideState.PAYMENT_COLLECTION) DROP_LOC else null

    Scaffold(
        containerColor = colors.background
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // ── Map Background ──
            DriverMapBackground(
                isOnline = isOnline,
                driverLocation = DRIVER_LOC,
                pickupLatLng = pickupForMap,
                dropLatLng = dropForMap,
                showRoute = showRoute
            )

            // ── Top Bar ──
            AnimatedVisibility(
                visible = rideState == RideState.IDLE,
                enter = slideInVertically { -100 } + fadeIn(),
                exit = slideOutVertically { -100 } + fadeOut()
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Driver Avatar
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .shadow(8.dp, CircleShape)
                                .clip(CircleShape)
                                .background(colors.surfaceElevated)
                                .border(2.dp, YellowPrimary, CircleShape)
                                .clickable { onProfileClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("😊", fontSize = 22.sp)
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 12.dp)
                        ) {
                            Text("Good Morning,", color = colors.textSecondary, fontSize = 11.sp)
                            Text(driverName, color = colors.textPrimary, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                        }

                        // Notification
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .shadow(4.dp, RoundedCornerShape(12.dp))
                                .clip(RoundedCornerShape(12.dp))
                                .background(colors.surface.copy(alpha = 0.95f))
                                .clickable { onNotificationClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Notifications, null, tint = colors.textPrimary, modifier = Modifier.size(20.dp))
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(RedAccent)
                                    .align(Alignment.TopEnd)
                                    .offset(x = 2.dp, y = (-2).dp)
                            )
                        }
                    }

                    // Earnings Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 8.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = YellowPrimary),
                        elevation = CardDefaults.cardElevation(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            EarningItem(label = "Today's Earning", value = displayEarning)
                            VerticalDivider(Modifier.height(44.dp).padding(vertical = 4.dp), color = BgDark.copy(alpha = 0.15f))
                            EarningItem(label = "Trips", value = tripCount)
                            VerticalDivider(Modifier.height(44.dp).padding(vertical = 4.dp), color = BgDark.copy(alpha = 0.15f))
                            EarningItem(label = "Rating", value = "$rating ★")
                        }
                    }
                }
            }

            // ── Location badge when online ──
            if (isOnline && rideState == RideState.IDLE) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .statusBarsPadding()
                        .padding(top = 8.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(GreenAccent.copy(alpha = 0.9f))
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("SEARCHING IN AHMEDABAD", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    }
                }
            }

            // ── Bottom Panels ──
            Box(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                AnimatedContent(
                    targetState = rideState,
                    transitionSpec = {
                        slideInVertically { it } + fadeIn() togetherWith slideOutVertically { it } + fadeOut()
                    },
                    label = "BottomPanel"
                ) { state ->
                    when (state) {
                        RideState.IDLE -> {
                            // Online/Offline Panel
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                                    .background(colors.surface)
                                    .padding(top = 12.dp, bottom = 8.dp, start = 20.dp, end = 20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(modifier = Modifier.width(36.dp).height(4.dp).clip(RoundedCornerShape(2.dp)).background(colors.divider))
                                Spacer(Modifier.height(14.dp))

                                // Status text
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .clip(CircleShape)
                                            .background(if (isOnline) GreenAccent else RedAccent)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = if (isOnline) "YOU ARE ONLINE" else "YOU ARE OFFLINE",
                                        color = if (isOnline) GreenAccent else colors.textSecondary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.5.sp
                                    )
                                }
                                Spacer(Modifier.height(14.dp))

                                // Go Online/Offline Button
                                Button(
                                    onClick = {
                                        isOnline = !isOnline
                                        buttonScale = 0.9f
                                    },
                                    modifier = Modifier
                                        .width(200.dp)
                                        .height(56.dp)
                                        .scale(animatedScale),
                                    shape = RoundedCornerShape(28.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isOnline) RedAccent else YellowPrimary,
                                        contentColor = if (isOnline) Color.White else BgDark
                                    ),
                                    elevation = ButtonDefaults.buttonElevation(12.dp)
                                ) {
                                    Icon(
                                        if (isOnline) Icons.Default.PowerSettingsNew else Icons.Default.PlayArrow,
                                        null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = if (isOnline) "GO OFFLINE" else "GO ONLINE",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 15.sp,
                                        letterSpacing = 1.sp
                                    )
                                }
                                LaunchedEffect(isOnline) { delay(100); buttonScale = 1f }
                                Spacer(Modifier.height(16.dp))

                                // Quick Stats
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    QuickStatCard("⏱️", "3h 20m", "Online", Modifier.weight(1f))
                                    QuickStatCard("🛣️", "42.5 km", "Driven", Modifier.weight(1f))
                                    QuickStatCard("✅", "92%", "Acceptance", Modifier.weight(1f))
                                }
                                Spacer(Modifier.height(8.dp))
                            }
                        }

                        RideState.REQUESTED -> {
                            TripRequestSheet(
                                pickupName = "Navrangpura",
                                pickupDetail = "Near Gujarat University, Ahmedabad",
                                pickupDist = "2.1 km",
                                dropName = "Satellite",
                                dropDetail = "Near Jodhpur Cross Roads, Ahmedabad",
                                tripDist = "6.5 km",
                                fare = "₹ 185",
                                eta = "~18 min",
                                onAccept = { rideState = RideState.EN_ROUTE_PICKUP },
                                onDecline = { rideState = RideState.IDLE },
                                onTimeout = { rideState = RideState.IDLE }
                            )
                        }

                        RideState.EN_ROUTE_PICKUP -> {
                            ActiveRideCard(
                                title = "NAVIGATING TO PICKUP",
                                duration = "5 min",
                                distance = "2.1 km",
                                address = "Navrangpura, Near Gujarat University",
                                actionLabel = "ARRIVED",
                                onActionClick = { rideState = RideState.ARRIVED_PICKUP },
                                showNavigateButton = true
                            )
                        }

                        RideState.ARRIVED_PICKUP -> {
                            var otp by remember { mutableStateOf("") }
                            var otpError by remember { mutableStateOf(false) }

                            ActiveRideCard(
                                title = "WAITING FOR PASSENGER",
                                duration = "Arrived",
                                distance = "0 km",
                                address = "Navrangpura, Near Gujarat University",
                                actionLabel = "START TRIP",
                                passengerName = "Aarav Patel",
                                onActionClick = {
                                    if (otp.length == 4) {
                                        otpError = false
                                        rideState = RideState.IN_TRIP
                                    } else {
                                        otpError = true
                                    }
                                }
                            ) {
                                Spacer(Modifier.height(16.dp))
                                // OTP Input
                                Text("Enter Pickup OTP", color = colors.textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                                Spacer(Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = otp,
                                    onValueChange = { if (it.length <= 4) otp = it; otpError = false },
                                    label = { Text("4-Digit OTP") },
                                    isError = otpError,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                    colors = wayGoTextFieldColors(),
                                    modifier = Modifier.fillMaxWidth().height(60.dp),
                                    singleLine = true,
                                    textStyle = LocalTextStyle.current.copy(
                                        textAlign = TextAlign.Center,
                                        fontSize = 22.sp,
                                        letterSpacing = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                if (otpError) {
                                    Text("Enter 4-digit OTP from passenger", color = RedAccent, fontSize = 11.sp, modifier = Modifier.padding(start = 14.dp, top = 4.dp))
                                }
                            }
                        }

                        RideState.IN_TRIP -> {
                            ActiveRideCard(
                                title = "NAVIGATING TO DROP-OFF",
                                duration = "18 min",
                                distance = "6.5 km",
                                address = "Satellite, Jodhpur Cross Roads",
                                actionLabel = "END TRIP",
                                passengerName = "Aarav Patel",
                                onActionClick = { rideState = RideState.PAYMENT_COLLECTION },
                                isDestructiveAction = true,
                                showNavigateButton = true
                            )
                        }

                        RideState.PAYMENT_COLLECTION -> {
                            ActiveRideCard(
                                title = "TRIP COMPLETED",
                                duration = "Trip Complete",
                                distance = "6.5 km",
                                address = "Satellite, Jodhpur Cross Roads",
                                actionLabel = "PAYMENT RECEIVED",
                                onActionClick = {
                                    rideState = RideState.IDLE
                                    isOnline = false
                                }
                            ) {
                                Spacer(Modifier.height(16.dp))
                                // Payment Summary
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(YellowPrimary.copy(alpha = 0.15f), YellowPrimary.copy(alpha = 0.05f))
                                            )
                                        )
                                        .border(1.5.dp, YellowPrimary.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                                        .padding(20.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("💰", fontSize = 28.sp)
                                        Spacer(Modifier.height(4.dp))
                                        Text("Collect Cash", color = colors.textSecondary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                        Text("₹185", color = YellowPrimary, fontSize = 48.sp, fontWeight = FontWeight.Black)
                                        Spacer(Modifier.height(8.dp))
                                        Row(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(colors.surfaceElevated)
                                                .padding(horizontal = 12.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text("💵", fontSize = 14.sp)
                                            Spacer(Modifier.width(6.dp))
                                            Text("Cash Payment", color = colors.textSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                        }
                                        Spacer(Modifier.height(4.dp))
                                        Text("Includes ₹20 platform fee", color = colors.textSecondary.copy(alpha = 0.6f), fontSize = 10.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ActiveRideCard(
    title: String,
    duration: String,
    distance: String,
    address: String,
    actionLabel: String,
    passengerName: String? = null,
    isDestructiveAction: Boolean = false,
    showNavigateButton: Boolean = false,
    onActionClick: () -> Unit,
    extraContent: @Composable () -> Unit = {}
) {
    val colors = LocalWayGoColors.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
            .background(colors.surface)
            .padding(top = 12.dp, bottom = 24.dp, start = 20.dp, end = 20.dp)
    ) {
        // Drag Handle
        Box(
            modifier = Modifier
                .width(36.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(colors.divider)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(16.dp))

        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(title, color = YellowPrimary, fontSize = 11.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
            if (showNavigateButton) {
                Spacer(Modifier.weight(1f))
                FilledTonalButton(
                    onClick = { /* Opens Google Maps navigation */ },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = BlueAccent.copy(alpha = 0.15f),
                        contentColor = BlueAccent
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Navigation, null, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Navigate", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        Spacer(Modifier.height(8.dp))

        // Time/Dist
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(duration, color = colors.textPrimary, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.width(12.dp))
            Text("•", color = colors.textSecondary, fontSize = 20.sp)
            Spacer(Modifier.width(12.dp))
            Text(distance, color = colors.textSecondary, fontSize = 20.sp, fontWeight = FontWeight.Medium)
        }

        Spacer(Modifier.height(16.dp))

        // Address Card
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = colors.surfaceElevated),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(GreenAccent.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.LocationOn, null, tint = GreenAccent, modifier = Modifier.size(20.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    if (passengerName != null) {
                        Text(passengerName, color = colors.textPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text(address, color = colors.textSecondary, fontSize = 12.sp)
                    } else {
                        Text(address, color = colors.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        extraContent()

        Spacer(Modifier.height(20.dp))

        // Action Button
        val btnColor = if (isDestructiveAction) RedAccent else YellowPrimary
        val txtColor = if (isDestructiveAction) Color.White else BgDark

        Button(
            onClick = onActionClick,
            modifier = Modifier.fillMaxWidth().height(58.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = btnColor, contentColor = txtColor),
            elevation = ButtonDefaults.buttonElevation(8.dp)
        ) {
            Text(actionLabel, fontWeight = FontWeight.Black, fontSize = 16.sp, letterSpacing = 1.sp)
            Spacer(Modifier.width(12.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
fun EarningItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = BgDark, fontSize = 18.sp, fontWeight = FontWeight.Black)
        Text(label, color = BgDark.copy(alpha = 0.6f), fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun QuickStatCard(icon: String, value: String, label: String, modifier: Modifier = Modifier) {
    val colors = LocalWayGoColors.current
    Card(
        modifier = modifier,
        shape    = RoundedCornerShape(14.dp),
        colors   = CardDefaults.cardColors(containerColor = colors.surfaceElevated)
    ) {
        Column(
            modifier            = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(icon, fontSize = 20.sp)
            Spacer(Modifier.height(4.dp))
            Text(value, color = colors.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(label, color = colors.textSecondary, fontSize = 9.sp)
        }
    }
}