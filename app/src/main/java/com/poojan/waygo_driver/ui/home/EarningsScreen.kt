package com.poojan.waygo_driver.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poojan.waygo_driver.data.model.TripHistoryItem
import com.poojan.waygo_driver.data.model.mockTrips
import com.poojan.waygo_driver.ui.theme.*

@Composable
fun EarningsScreen() {
    val colors = LocalWayGoColors.current
    var showWithdrawDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text("Earnings & Wallet", color = colors.textPrimary, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
        }

        LazyColumn(
            contentPadding = PaddingValues(bottom = 100.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Wallet Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = YellowPrimary),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Available Balance", color = BgDark.copy(alpha = 0.7f), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("₹2,450.00", color = BgDark, fontSize = 42.sp, fontWeight = FontWeight.Black)
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { showWithdrawDialog = true },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BgDark, contentColor = YellowPrimary)
                        ) {
                            Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Withdraw to Bank", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Weekly Overview
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.surface),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("This Week's Overview", color = colors.textPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            DetailedEarningStat("Trips", "42", Icons.Default.DirectionsCar)
                            DetailedEarningStat("Hours", "36h", Icons.Default.Schedule)
                            DetailedEarningStat("Earnings", "₹8,450", Icons.Default.TrendingUp)
                        }
                    }
                }
            }

            // Transactions Title
            item {
                Text("Recent Transactions", color = colors.textPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp, bottom = 4.dp))
            }

            // Transactions
            items(mockTrips) { transaction ->
                DetailedTransactionRow(transaction)
            }
        }
    }

    if (showWithdrawDialog) {
        WithdrawDialog(
            balance = "₹2,450.00",
            onDismiss = { showWithdrawDialog = false },
            onConfirm = { showWithdrawDialog = false }
        )
    }
}

@Composable
fun DetailedEarningStat(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    val colors = LocalWayGoColors.current
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier.size(48.dp).clip(CircleShape).background(colors.surfaceElevated),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = YellowPrimary, modifier = Modifier.size(24.dp))
        }
        Spacer(Modifier.height(8.dp))
        Text(value, color = colors.textPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(label, color = colors.textSecondary, fontSize = 12.sp)
    }
}

@Composable
fun DetailedTransactionRow(trip: TripHistoryItem) {
    val colors = LocalWayGoColors.current
    var isExpanded by remember { mutableStateOf(false) }
    val isCredit = trip.status == "Completed"
    val rotationState by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f)

    Card(
        modifier = Modifier.fillMaxWidth().clickable { isExpanded = !isExpanded },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isCredit) GreenAccent.copy(alpha = 0.15f) else colors.surfaceElevated),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (isCredit) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                        contentDescription = null,
                        tint = if (isCredit) GreenAccent else colors.textSecondary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column(
                    modifier = Modifier.weight(1f).padding(horizontal = 12.dp)
                ) {
                    Text(if (isCredit) "Trip Payment" else "Trip Cancelled", color = colors.textPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Text("${trip.date} • ${trip.time}", color = colors.textSecondary, fontSize = 12.sp)
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        if (isCredit) "+ ${trip.amount}" else "₹0",
                        color = if (isCredit) GreenAccent else colors.textSecondary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(Modifier.height(4.dp))
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = colors.textSecondary,
                        modifier = Modifier.size(20.dp).rotate(rotationState)
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    HorizontalDivider(color = colors.divider, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Transaction Details", color = colors.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    EarningsReceiptRow("Trip Fare", "₹${"%.2f".format(trip.totalFare)}")
                    EarningsReceiptRow("Platform Fee", "-₹${"%.2f".format(trip.platformFee)}", isDeduction = true)
                    if (trip.surgeAmount > 0) {
                        EarningsReceiptRow("Surge Pricing", "+₹${"%.2f".format(trip.surgeAmount)}")
                    }
                    HorizontalDivider(color = colors.divider, modifier = Modifier.padding(vertical = 8.dp))
                    EarningsReceiptRow("Total Earnings", "₹${"%.2f".format(trip.driverEarnings)}", isBold = true, isGreen = true)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Trip ID: ${trip.id}", color = colors.textSecondary, fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
fun EarningsReceiptRow(label: String, value: String, isDeduction: Boolean = false, isBold: Boolean = false, isGreen: Boolean = false) {
    val colors = LocalWayGoColors.current
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = if (isBold) colors.textPrimary else colors.textSecondary, fontSize = 13.sp, fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal)
        Text(value, color = if (isGreen) GreenAccent else if (isDeduction) RedAccent else colors.textPrimary, fontSize = 13.sp, fontWeight = if (isBold) FontWeight.Bold else FontWeight.Medium)
    }
}

@Composable
fun WithdrawDialog(balance: String, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    val colors = LocalWayGoColors.current
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = colors.surface,
        titleContentColor = colors.textPrimary,
        textContentColor = colors.textSecondary,
        title = { Text("Withdraw Funds", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text("Are you sure you want to withdraw your available balance of $balance to your linked bank account ending in **4521**?")
                Spacer(Modifier.height(16.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = colors.surfaceElevated),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AccountBalance, contentDescription = null, tint = YellowPrimary)
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("HDFC Bank", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("•••• 4521", color = colors.textSecondary, fontSize = 12.sp)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = YellowPrimary, contentColor = BgDark)
            ) {
                Text("Confirm Withdraw", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, colors = ButtonDefaults.textButtonColors(contentColor = colors.textPrimary)) {
                Text("Cancel")
            }
        }
    )
}
