package com.poojan.waygo_driver.ui.trip

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poojan.waygo_driver.data.model.TripHistoryItem
import com.poojan.waygo_driver.data.model.mockTrips
import com.poojan.waygo_driver.ui.home.TripRouteMap
import com.poojan.waygo_driver.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun TripsScreen() {
    val colors = LocalWayGoColors.current
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("All Trips", "Completed", "Cancelled")

    val filteredTrips = remember(selectedTabIndex) {
        when (selectedTabIndex) {
            1 -> mockTrips.filter { it.status == "Completed" }
            2 -> mockTrips.filter { it.status == "Cancelled" }
            else -> mockTrips
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(colors = listOf(colors.surface, colors.background))
                )
                .padding(top = 40.dp, bottom = 10.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text("Your Trips", color = colors.textPrimary, fontSize = 28.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                Text("Track your recent activity and earnings.", color = colors.textSecondary, fontSize = 13.sp, modifier = Modifier.padding(top = 4.dp))

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MiniStatBadge(title = "Total Trips", value = "1,248", icon = Icons.Default.DirectionsCar)
                    MiniStatBadge(title = "Acceptance", value = "92%", icon = Icons.Default.CheckCircle)
                }
            }
        }

        // Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.Transparent,
            contentColor = YellowPrimary,
            edgePadding = 20.dp,
            divider = {},
            indicator = { tabPositions ->
                Box(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .height(3.dp)
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp))
                        .background(YellowPrimary)
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                val selected = selectedTabIndex == index
                val color by animateColorAsState(if (selected) YellowPrimary else colors.textSecondary, label = "tabColor")
                Tab(
                    selected = selected,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            color = color,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // Trip List
        AnimatedContent(
            targetState = filteredTrips,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(150))
            },
            modifier = Modifier.weight(1f),
            label = "tripList"
        ) { currentTrips ->
            if (currentTrips.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("📭", fontSize = 48.sp)
                        Spacer(Modifier.height(12.dp))
                        Text("No trips found.", color = colors.textSecondary, fontSize = 16.sp)
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    currentTrips.forEach { trip ->
                        PremiumTripCard(trip = trip)
                    }
                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun MiniStatBadge(title: String, value: String, icon: ImageVector) {
    val colors = LocalWayGoColors.current
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(colors.surfaceElevated.copy(alpha = 0.5f))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = YellowPrimary, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(8.dp))
        Column {
            Text(title, color = colors.textSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            Text(value, color = colors.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.Black)
        }
    }
}

@Composable
fun PremiumTripCard(trip: TripHistoryItem) {
    val colors = LocalWayGoColors.current
    var expanded by remember { mutableStateOf(false) }
    val isCompleted = trip.status == "Completed"
    val glowColor = if (isCompleted) GreenAccent else RedAccent

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            .then(
                if (expanded) Modifier.border(
                    width = 1.5.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(glowColor.copy(alpha = 0.5f), Color.Transparent)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) else Modifier
            )
            .clickable(
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                indication = null
            ) { expanded = !expanded },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = if (expanded) 8.dp else 2.dp)
    ) {
        Column {
            // Top Summary
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isCompleted) GreenAccent.copy(alpha = 0.15f) else RedAccent.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isCompleted) Icons.Default.Check else Icons.Default.Close,
                        contentDescription = null,
                        tint = if (isCompleted) GreenAccent else RedAccent
                    )
                }

                Spacer(Modifier.width(14.dp))

                Column(Modifier.weight(1f)) {
                    Text(trip.date, color = colors.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(
                        trip.status.uppercase(),
                        color = if (isCompleted) GreenAccent else RedAccent,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text("₹${trip.driverEarnings}", color = YellowPrimary, fontSize = 18.sp, fontWeight = FontWeight.Black)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AccessTime, contentDescription = null, tint = colors.textSecondary, modifier = Modifier.size(10.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(trip.duration, color = colors.textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }

            // Expanded Content
            if (expanded) {
                HorizontalDivider(color = colors.background, thickness = 2.dp)

                // Map View with route
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(0.dp))
                ) {
                    TripRouteMap(
                        startLatLng = trip.startLatLng,
                        endLatLng = trip.endLatLng,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Route overlay
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomStart)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, colors.surface.copy(alpha = 0.9f))
                                )
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(GreenAccent))
                            Box(modifier = Modifier.width(2.dp).height(20.dp).background(colors.divider))
                            Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(2.dp)).background(YellowPrimary))
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.height(44.dp)
                        ) {
                            Text(trip.startLoc, color = colors.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                            Text(trip.endLoc, color = colors.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                        }
                    }
                }

                // Segmented Details
                TripDetailsSegmentedControl(trip)
            }
        }
    }
}

@Composable
fun TripDetailsSegmentedControl(trip: TripHistoryItem) {
    val colors = LocalWayGoColors.current
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(colors.surfaceElevated),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SegmentButton("TRIP DETAILS", pagerState.currentPage == 0, Modifier.weight(1f)) {
                coroutineScope.launch { pagerState.animateScrollToPage(0) }
            }
            SegmentButton("FARE RECEIPT", pagerState.currentPage == 1, Modifier.weight(1f)) {
                coroutineScope.launch { pagerState.animateScrollToPage(1) }
            }
        }

        Spacer(Modifier.height(16.dp))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth().animateContentSize()
        ) { page ->
            when (page) {
                0 -> {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(Modifier.fillMaxWidth()) {
                            DetailItem(title = "Passenger", value = trip.passengerName, icon = "👤", modifier = Modifier.weight(1f))
                            DetailItem(title = "Distance", value = trip.distance, icon = "🛣️", modifier = Modifier.weight(1f))
                        }
                        Row(Modifier.fillMaxWidth()) {
                            DetailItem(title = "Trip ID", value = trip.id, icon = "🧾", modifier = Modifier.weight(1f))
                            DetailItem(title = "Service", value = "WayGo Economy", icon = "🚗", modifier = Modifier.weight(1f))
                        }
                    }
                }
                1 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(colors.surfaceElevated)
                            .padding(16.dp)
                    ) {
                        TripReceiptRow("Base Fare", "₹${trip.baseFare}")
                        TripReceiptRow("Distance Fare", "₹${trip.distanceFare}")
                        TripReceiptRow("Time Fare", "₹${trip.timeFare}")
                        if (trip.surgeAmount > 0.0) TripReceiptRow("Surge Pricing", "₹${trip.surgeAmount}", isPositive = true)
                        TripReceiptRow("Taxes", "₹${trip.taxAmount}")
                        HorizontalDivider(color = colors.divider, modifier = Modifier.padding(vertical = 8.dp))
                        TripReceiptRow("Total Fare Customer Paid", "₹${trip.totalFare}", isBold = true)

                        Spacer(Modifier.height(8.dp))
                        Box(modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(YellowPrimary.copy(alpha = 0.15f)).padding(10.dp)) {
                            TripReceiptRow("Your Net Earnings", "₹${trip.driverEarnings}", isBold = true, highlightColor = YellowPrimary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SegmentButton(text: String, isSelected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    val bgColor by animateColorAsState(if (isSelected) YellowPrimary else Color.Transparent, label = "bg")
    val textColor by animateColorAsState(if (isSelected) BgDark else LocalWayGoColors.current.textSecondary, label = "text")

    Box(
        modifier = modifier
            .fillMaxHeight()
            .padding(2.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = textColor, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
    }
}

@Composable
fun DetailItem(title: String, value: String, icon: String, modifier: Modifier = Modifier) {
    val colors = LocalWayGoColors.current
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier.size(36.dp).clip(CircleShape).background(colors.surfaceElevated),
            contentAlignment = Alignment.Center
        ) {
            Text(icon, fontSize = 16.sp)
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(title, color = colors.textSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text(value, color = colors.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun TripReceiptRow(label: String, value: String, isPositive: Boolean = false, isBold: Boolean = false, highlightColor: Color = LocalWayGoColors.current.textPrimary) {
    val colors = LocalWayGoColors.current
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = if (isBold) highlightColor else colors.textSecondary,
            fontSize = if (isBold) 14.sp else 12.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Medium
        )
        Text(
            text = value,
            color = if (isPositive) GreenAccent else highlightColor,
            fontSize = if (isBold) 14.sp else 12.sp,
            fontWeight = if (isBold) FontWeight.Black else FontWeight.SemiBold
        )
    }
}
