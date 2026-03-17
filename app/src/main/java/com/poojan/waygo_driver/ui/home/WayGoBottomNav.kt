package com.poojan.waygo_driver.ui.home

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.poojan.waygo_driver.Routes
import com.poojan.waygo_driver.ui.theme.*

@Composable
fun WayGoBottomNav(navController: NavController) {
    val colors = LocalWayGoColors.current
    val items = listOf(
        Triple("Home", Icons.Default.Home, Routes.HOME),
        Triple("Earnings", Icons.Default.AttachMoney, Routes.EARNINGS),
        Triple("Trips", Icons.Default.DirectionsCar, Routes.TRIPS),
        Triple("Profile", Icons.Default.Person, Routes.PROFILE)
    )

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    if (currentRoute !in listOf(Routes.HOME, Routes.EARNINGS, Routes.TRIPS, Routes.PROFILE)) {
        return
    }

    NavigationBar(
        containerColor = colors.surfaceElevated,
        tonalElevation = 0.dp
    ) {
        items.forEach { (label, icon, route) ->
            val selected = currentRoute == route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(icon, contentDescription = label, modifier = Modifier.size(22.dp)) },
                label = { Text(label, fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = YellowPrimary,
                    selectedTextColor = YellowPrimary,
                    unselectedIconColor = colors.textSecondary,
                    unselectedTextColor = colors.textSecondary,
                    indicatorColor = YellowPrimary.copy(alpha = 0.15f)
                )
            )
        }
    }
}
