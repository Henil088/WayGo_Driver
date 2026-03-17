package com.poojan.waygo_driver.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*
import com.poojan.waygo_driver.ui.theme.LocalWayGoColors

// Ahmedabad center
val AHMEDABAD_CENTER = LatLng(23.0225, 72.5714)

// Ahmedabad bounds (restrict map area)
val AHMEDABAD_BOUNDS = LatLngBounds(
    LatLng(22.9200, 72.4300), // SW corner
    LatLng(23.1300, 72.7000)  // NE corner
)

@Composable
fun DriverMapBackground(
    isOnline: Boolean = false,
    driverLocation: LatLng = AHMEDABAD_CENTER,
    pickupLatLng: LatLng? = null,
    dropLatLng: LatLng? = null,
    showRoute: Boolean = false
) {
    val colors = LocalWayGoColors.current

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(AHMEDABAD_CENTER, 13f)
    }

    // Animate camera to show route when pickup/drop are set
    LaunchedEffect(pickupLatLng, dropLatLng, showRoute) {
        if (showRoute && pickupLatLng != null && dropLatLng != null) {
            val bounds = LatLngBounds.builder()
                .include(pickupLatLng)
                .include(dropLatLng)
                .include(driverLocation)
                .build()
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngBounds(bounds, 120),
                durationMs = 800
            )
        } else if (isOnline) {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(driverLocation, 15f),
                durationMs = 600
            )
        }
    }

    val darkMapStyle = """
        [
          {"elementType":"geometry","stylers":[{"color":"#12181E"}]},
          {"elementType":"labels.text.fill","stylers":[{"color":"#8c9fb6"}]},
          {"elementType":"labels.text.stroke","stylers":[{"color":"#0d1117"}]},
          {"featureType":"administrative.country","elementType":"geometry.stroke","stylers":[{"color":"#1a2229"}]},
          {"featureType":"poi","elementType":"geometry","stylers":[{"color":"#1a2229"}]},
          {"featureType":"poi","elementType":"labels","stylers":[{"visibility":"off"}]},
          {"featureType":"road","elementType":"geometry","stylers":[{"color":"#1a2633"}]},
          {"featureType":"road","elementType":"geometry.stroke","stylers":[{"color":"#1a2229"}]},
          {"featureType":"road.highway","elementType":"geometry","stylers":[{"color":"#243040"}]},
          {"featureType":"transit","stylers":[{"visibility":"off"}]},
          {"featureType":"water","elementType":"geometry","stylers":[{"color":"#0d1117"}]}
        ]
    """.trimIndent()

    val lightMapStyle = """
        [
          {"featureType":"poi","elementType":"labels","stylers":[{"visibility":"off"}]},
          {"featureType":"transit","stylers":[{"visibility":"off"}]},
          {"featureType":"road.highway","elementType":"geometry","stylers":[{"color":"#FFE082"}]},
          {"featureType":"water","elementType":"geometry","stylers":[{"color":"#B3E5FC"}]}
        ]
    """.trimIndent()

    val mapProperties = remember(colors.isLight) {
        MapProperties(
            mapStyleOptions = MapStyleOptions(if (colors.isLight) lightMapStyle else darkMapStyle),
            isMyLocationEnabled = false,
            latLngBoundsForCameraTarget = AHMEDABAD_BOUNDS
        )
    }

    val uiSettings = remember {
        MapUiSettings(
            zoomControlsEnabled = false,
            compassEnabled = false,
            myLocationButtonEnabled = false,
            mapToolbarEnabled = false
        )
    }

    val bgColor = if (colors.isLight) Color(0xFFE8EAF0) else Color(0xFF0D1117)

    Box(modifier = Modifier.fillMaxSize().background(bgColor)) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = uiSettings
        ) {
            // Driver location marker (only when online)
            if (isOnline) {
                Marker(
                    state = MarkerState(position = driverLocation),
                    title = "You",
                    snippet = "Your current location"
                )
            }

            // Pickup marker
            if (pickupLatLng != null) {
                Marker(
                    state = MarkerState(position = pickupLatLng),
                    title = "Pickup",
                    snippet = "Pickup location"
                )
            }

            // Drop marker
            if (dropLatLng != null) {
                Marker(
                    state = MarkerState(position = dropLatLng),
                    title = "Drop-off",
                    snippet = "Drop-off location"
                )
            }

            // Route polyline
            if (showRoute && pickupLatLng != null && dropLatLng != null) {
                // Create smooth route points between pickup and drop
                val routePoints = generateRoutePoints(pickupLatLng, dropLatLng)
                Polyline(
                    points = routePoints,
                    color = Color(0xFFFFD600),
                    width = 14f
                )
                // Route shadow
                Polyline(
                    points = routePoints,
                    color = Color(0x40FFD600),
                    width = 24f
                )
            }

            // Driver to pickup route (when en-route)
            if (showRoute && pickupLatLng != null && isOnline) {
                val driverToPickup = generateRoutePoints(driverLocation, pickupLatLng)
                Polyline(
                    points = driverToPickup,
                    color = Color(0xFF00C853),
                    width = 10f
                )
            }
        }
    }
}

/** Generate realistic-looking route points between two locations */
fun generateRoutePoints(start: LatLng, end: LatLng): List<LatLng> {
    val points = mutableListOf<LatLng>()
    val steps = 8
    for (i in 0..steps) {
        val fraction = i.toFloat() / steps
        val lat = start.latitude + (end.latitude - start.latitude) * fraction
        val lng = start.longitude + (end.longitude - start.longitude) * fraction
        // Add slight curve offset for realism
        val offset = Math.sin(fraction * Math.PI) * 0.003
        points.add(LatLng(lat + offset, lng))
    }
    return points
}

/** Small static map for trip history cards */
@Composable
fun TripRouteMap(
    startLatLng: LatLng,
    endLatLng: LatLng,
    modifier: Modifier = Modifier
) {
    val colors = LocalWayGoColors.current
    val bounds = LatLngBounds.builder()
        .include(startLatLng)
        .include(endLatLng)
        .build()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(bounds.center, 13f)
    }

    LaunchedEffect(Unit) {
        cameraPositionState.animate(
            CameraUpdateFactory.newLatLngBounds(bounds, 60),
            durationMs = 500
        )
    }

    val darkMapStyle = """
        [
          {"elementType":"geometry","stylers":[{"color":"#12181E"}]},
          {"elementType":"labels","stylers":[{"visibility":"off"}]},
          {"featureType":"road","elementType":"geometry","stylers":[{"color":"#1a2633"}]},
          {"featureType":"water","elementType":"geometry","stylers":[{"color":"#0d1117"}]}
        ]
    """.trimIndent()

    val lightMapStyle = """
        [
          {"elementType":"labels","stylers":[{"visibility":"off"}]},
          {"featureType":"water","elementType":"geometry","stylers":[{"color":"#B3E5FC"}]}
        ]
    """.trimIndent()

    val mapProperties = remember(colors.isLight) {
        MapProperties(
            mapStyleOptions = MapStyleOptions(if (colors.isLight) lightMapStyle else darkMapStyle),
            isMyLocationEnabled = false
        )
    }

    val uiSettings = remember {
        MapUiSettings(
            zoomControlsEnabled = false,
            compassEnabled = false,
            myLocationButtonEnabled = false,
            mapToolbarEnabled = false,
            scrollGesturesEnabled = false,
            zoomGesturesEnabled = false,
            tiltGesturesEnabled = false,
            rotationGesturesEnabled = false
        )
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = mapProperties,
        uiSettings = uiSettings
    ) {
        // Start marker
        Marker(
            state = MarkerState(position = startLatLng),
            title = "Pickup"
        )
        // End marker
        Marker(
            state = MarkerState(position = endLatLng),
            title = "Drop-off"
        )
        // Route
        val routePoints = generateRoutePoints(startLatLng, endLatLng)
        Polyline(
            points = routePoints,
            color = Color(0xFFFFD600),
            width = 10f
        )
    }
}
