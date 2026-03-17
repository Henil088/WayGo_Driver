package com.poojan.waygo_driver.ui.trip

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poojan.waygo_driver.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun TripRequestSheet(
    pickupName  : String = "Navrangpura",
    pickupDetail: String = "Near Gujarat University, Ahmedabad",
    pickupDist  : String = "2.1 km",
    dropName    : String = "Satellite",
    dropDetail  : String = "Near Jodhpur Cross Roads, Ahmedabad",
    tripDist    : String = "6.5 km",
    fare        : String = "₹ 185",
    eta         : String = "~18 min",
    paymentType : String = "Cash",
    rideType    : String = "Economy",
    totalSeconds: Int    = 30,
    onAccept    : () -> Unit = {},
    onDecline   : () -> Unit = {},
    onTimeout   : () -> Unit = {}
) {
    val colors = LocalWayGoColors.current
    var secondsLeft by remember { mutableStateOf(totalSeconds) }

    LaunchedEffect(Unit) {
        while (secondsLeft > 0) {
            delay(1000)
            secondsLeft--
        }
        onTimeout()
    }

    var fareVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { delay(200); fareVisible = true }
    val fareScale by animateFloatAsState(
        targetValue = if (fareVisible) 1f else 0f,
        animationSpec = spring(Spring.DampingRatioMediumBouncy),
        label = "fareScale"
    )

    val ripple by rememberInfiniteTransition(label = "ripple").animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
        label = "rippleScale"
    )

    val progress = secondsLeft.toFloat() / totalSeconds
    val timerColor = if (secondsLeft > 10) YellowPrimary else RedAccent

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // TOP CARD: Timer, Fare, Alert
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .shadow(16.dp, RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .background(colors.surface.copy(alpha = 0.95f))
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Warning text
                Text("NEW TRIP", color = colors.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)

                // Timer
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.size(44.dp),
                        color = timerColor,
                        strokeWidth = 4.dp,
                        trackColor = colors.surfaceElevated,
                        strokeCap = StrokeCap.Round
                    )
                    Text(
                        text = secondsLeft.toString(),
                        color = timerColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Fare Badge
            Box(
                modifier = Modifier
                    .scale(fareScale)
                    .clip(RoundedCornerShape(100.dp))
                    .background(YellowPrimary)
                    .padding(horizontal = 32.dp, vertical = 8.dp)
            ) {
                Text(fare, color = BgDark, fontSize = 32.sp, fontWeight = FontWeight.Black)
            }
            Spacer(Modifier.height(12.dp))
            
            // Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TripChip(icon = "💵", label = paymentType)
                Spacer(Modifier.width(8.dp))
                TripChip(icon = "🚗", label = rideType)
                Spacer(Modifier.width(8.dp))
                TripChip(icon = "⏱️", label = eta)
            }
        }

        // BOTTOM CARD: Route & Actions
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(24.dp, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(colors.surface.copy(alpha = 0.98f))
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            // Route Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surfaceElevated)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Pickup
                    Row(verticalAlignment = Alignment.Top) {
                        Box(modifier = Modifier.padding(top = 4.dp).size(12.dp).clip(CircleShape).background(GreenAccent))
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text("PICKUP", color = GreenAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                            Text(pickupName, color = colors.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text(pickupDetail, color = colors.textSecondary, fontSize = 12.sp)
                        }
                        Text(pickupDist, color = YellowPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Box(
                        modifier = Modifier
                            .padding(start = 5.dp, top = 4.dp, bottom = 4.dp)
                            .width(2.dp)
                            .height(24.dp)
                            .background(colors.divider)
                    )

                    // Drop
                    Row(verticalAlignment = Alignment.Top) {
                        Box(modifier = Modifier.padding(top = 4.dp).size(12.dp).clip(RoundedCornerShape(3.dp)).background(YellowPrimary))
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text("DROP", color = YellowPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                            Text(dropName, color = colors.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text(dropDetail, color = colors.textSecondary, fontSize = 12.sp)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(tripDist, color = colors.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                            Text("Est. Trip", color = colors.textSecondary, fontSize = 10.sp)
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDecline,
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.textPrimary),
                    border = BorderStroke(2.dp, colors.divider)
                ) {
                    Text("DECLINE", fontWeight = FontWeight.Bold, fontSize = 14.sp, letterSpacing = 0.5.sp)
                }

                Button(
                    onClick = onAccept,
                    modifier = Modifier.weight(1.5f).height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = YellowPrimary, contentColor = BgDark),
                    elevation = ButtonDefaults.buttonElevation(8.dp)
                ) {
                    Text("ACCEPT RIDE", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, letterSpacing = 0.5.sp)
                }
            }
        }
    }
}

@Composable
fun TripChip(icon: String, label: String) {
    val colors = LocalWayGoColors.current
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp))
            .background(colors.surfaceElevated)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(icon, fontSize = 12.sp)
        Spacer(Modifier.width(4.dp))
        Text(label, color = colors.textPrimary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
    }
}