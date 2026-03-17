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
import com.google.maps.android.PolyUtil
import com.poojan.waygo_driver.BuildConfig
import com.poojan.waygo_driver.network.NetworkClient
import com.poojan.waygo_driver.ui.theme.LocalWayGoColors
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.border
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

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
    myLocationEnabled: Boolean = false,
    rideState: RideState = RideState.IDLE,
    driverLocation: LatLng = AHMEDABAD_CENTER,
    pickupLatLng: LatLng? = null,
    dropLatLng: LatLng? = null,
    showRoute: Boolean = false,
    mapPadding: PaddingValues = PaddingValues(0.dp),
    onRouteInfoCalculated: (String, String) -> Unit = { _, _ -> },
    onIsFetchingRoute: (Boolean) -> Unit = {},
    recenterTrigger: Int = 0
) {
    val colors = LocalWayGoColors.current

    var driverToPickupRoute by remember { mutableStateOf<List<LatLng>?>(null) }
    var pickupToDropRoute by remember { mutableStateOf<List<LatLng>?>(null) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(AHMEDABAD_CENTER, 13f)
    }

    // A state to prevent endless looping API requests
    var lastRequestedPickup by remember { mutableStateOf<LatLng?>(null) }
    var lastRequestedDrop by remember { mutableStateOf<LatLng?>(null) }

    // Manual Re-center logic when My Location is clicked
    LaunchedEffect(recenterTrigger) {
        if (recenterTrigger > 0) {
            if (showRoute && pickupLatLng != null) {
                val boundsBuilder = LatLngBounds.builder()
                    .include(pickupLatLng)
                    .include(driverLocation)
                if (dropLatLng != null) boundsBuilder.include(dropLatLng)
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 120),
                    durationMs = 800
                )
            } else if (isOnline) {
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngZoom(driverLocation, 15f),
                    durationMs = 600
                )
            }
        }
    }

    // Animate camera to show route when pickup/drop are set
    LaunchedEffect(pickupLatLng, dropLatLng, showRoute) {
        if (showRoute && pickupLatLng != null) {
            val boundsBuilder = LatLngBounds.builder()
                .include(pickupLatLng)
                .include(driverLocation)
            
            if (dropLatLng != null) boundsBuilder.include(dropLatLng)
            
            val bounds = boundsBuilder.build()
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngBounds(bounds, 120),
                durationMs = 800
            )

            // Cache check to avoid API spamming
            if (pickupLatLng != lastRequestedPickup || dropLatLng != lastRequestedDrop) {
                lastRequestedPickup = pickupLatLng
                lastRequestedDrop = dropLatLng
                onIsFetchingRoute(true)

                // Fetch real road routes
                try {
                    val apiKey = BuildConfig.MAPS_API_KEY
                    
                    // Driver to Pickup
                    val p1 = NetworkClient.directionsApi.getDirections(
                        origin = "${driverLocation.latitude},${driverLocation.longitude}",
                        destination = "${pickupLatLng.latitude},${pickupLatLng.longitude}",
                        apiKey = apiKey
                    )
                    if (p1.routes.isNotEmpty()) {
                        driverToPickupRoute = PolyUtil.decode(p1.routes[0].overview_polyline.points)
                    }

                    // Pickup to Drop
                    if (dropLatLng != null) {
                        val p2 = NetworkClient.directionsApi.getDirections(
                            origin = "${pickupLatLng.latitude},${pickupLatLng.longitude}",
                            destination = "${dropLatLng.latitude},${dropLatLng.longitude}",
                            apiKey = apiKey
                        )
                        if (p2.routes.isNotEmpty()) {
                            pickupToDropRoute = PolyUtil.decode(p2.routes[0].overview_polyline.points)
                            val leg = p2.routes[0].legs[0]
                            onRouteInfoCalculated(leg.distance.text, leg.duration.text)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    onIsFetchingRoute(false)
                }
            }
        } else if (isOnline) {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(driverLocation, 15f),
                durationMs = 600
            )
            driverToPickupRoute = null
            pickupToDropRoute = null
            lastRequestedPickup = null
            lastRequestedDrop = null
        } else {
            driverToPickupRoute = null
            pickupToDropRoute = null
            lastRequestedPickup = null
            lastRequestedDrop = null
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

    val mapProperties = remember(colors.isLight, myLocationEnabled) {
        MapProperties(
            mapStyleOptions = MapStyleOptions(if (colors.isLight) lightMapStyle else darkMapStyle),
            isMyLocationEnabled = myLocationEnabled,
            latLngBoundsForCameraTarget = AHMEDABAD_BOUNDS
        )
    }

    val uiSettings = remember(myLocationEnabled) {
        MapUiSettings(
            zoomControlsEnabled = false,
            compassEnabled = false,
            myLocationButtonEnabled = false, // Disabled here so we can build our own floating button securely
            mapToolbarEnabled = false
        )
    }

    val bgColor = if (colors.isLight) Color(0xFFE8EAF0) else Color(0xFF0D1117)

    Box(modifier = Modifier.fillMaxSize().background(bgColor)) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = uiSettings,
            contentPadding = mapPadding
        ) {
            // Driver location marker (only when online)
            if (isOnline) {
                MarkerComposable(
                    state = MarkerState(position = driverLocation),
                    title = "You",
                    onClick = { true }
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .shadow(4.dp, CircleShape)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(2.dp, colors.primary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.DirectionsCar, contentDescription = null, tint = colors.primary, modifier = Modifier.size(24.dp))
                    }
                }
            }

            // Pickup marker
            if (pickupLatLng != null) {
                MarkerComposable(
                    state = MarkerState(position = pickupLatLng),
                    title = "Pickup",
                    onClick = { true }
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .shadow(4.dp, CircleShape)
                            .clip(CircleShape)
                            .background(Color(0xFF00C853))
                            .border(2.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Place, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                }
            }

            // Drop marker
            if (dropLatLng != null) {
                MarkerComposable(
                    state = MarkerState(position = dropLatLng),
                    title = "Drop-off",
                    onClick = { true }
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .shadow(4.dp, CircleShape)
                            .clip(CircleShape)
                            .background(Color(0xFFFFD600))
                            .border(2.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Place, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                }
            }

            // Route polyline (Pickup to Drop - The Trip)
            if (showRoute && pickupToDropRoute != null) {
                val segmentColor = if (rideState == RideState.IN_TRIP || rideState == RideState.REQUESTED) colors.primary else colors.textSecondary.copy(alpha = 0.3f)
                Polyline(points = pickupToDropRoute!!, color = segmentColor.copy(alpha = 0.2f), width = 24f) // shadow
                Polyline(points = pickupToDropRoute!!, color = segmentColor, width = 14f, startCap = RoundCap(), endCap = RoundCap())
            } else if (showRoute && pickupLatLng != null && dropLatLng != null) {
                val routePoints = generateRoutePoints(pickupLatLng, dropLatLng)
                Polyline(points = routePoints, color = colors.primary.copy(alpha = 0.2f), width = 24f)
                Polyline(points = routePoints, color = colors.primary, width = 14f, startCap = RoundCap(), endCap = RoundCap())
            }

            // Driver to pickup route (Navigation part)
            if (showRoute && driverToPickupRoute != null && isOnline) {
                val segmentColor = if (rideState == RideState.EN_ROUTE_PICKUP || rideState == RideState.REQUESTED) Color(0xFF00C853) else Color(0x4000C853)
                Polyline(points = driverToPickupRoute!!, color = segmentColor, width = 10f, startCap = RoundCap(), endCap = RoundCap())
            } else if (showRoute && pickupLatLng != null && isOnline) {
                val driverToPickup = generateRoutePoints(driverLocation, pickupLatLng)
                Polyline(points = driverToPickup, color = Color(0xFF00C853), width = 10f, startCap = RoundCap(), endCap = RoundCap())
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

/** Small static map for trip history cards, with optional fullscreen support */
@Composable
fun TripRouteMap(
    startLatLng: LatLng,
    endLatLng: LatLng,
    isFullScreen: Boolean = false,
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

    var routePoints by remember { mutableStateOf<List<LatLng>?>(null) }

    LaunchedEffect(Unit) {
        cameraPositionState.animate(
            CameraUpdateFactory.newLatLngBounds(bounds, 120),
            durationMs = 800
        )
        try {
            val response = NetworkClient.directionsApi.getDirections(
                origin = "${startLatLng.latitude},${startLatLng.longitude}",
                destination = "${endLatLng.latitude},${endLatLng.longitude}",
                apiKey = BuildConfig.MAPS_API_KEY
            )
            if (response.routes.isNotEmpty()) {
                routePoints = PolyUtil.decode(response.routes[0].overview_polyline.points)
            }
        } catch (e: Exception) {
            e.printStackTrace()
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

    val mapProperties = remember(colors.isLight) {
        MapProperties(
            mapStyleOptions = MapStyleOptions(darkMapStyle), // Force dark map to match home style for premium look
            isMyLocationEnabled = false
        )
    }

    val uiSettings = remember(isFullScreen) {
        MapUiSettings(
            zoomControlsEnabled = false,
            compassEnabled = false,
            myLocationButtonEnabled = false,
            mapToolbarEnabled = false,
            scrollGesturesEnabled = isFullScreen,
            zoomGesturesEnabled = isFullScreen,
            tiltGesturesEnabled = isFullScreen,
            rotationGesturesEnabled = isFullScreen
        )
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = mapProperties,
        uiSettings = uiSettings
    ) {
        if (isFullScreen) {
            MarkerComposable(
                state = MarkerState(position = startLatLng),
                title = "Pickup"
            ) {
                Box(
                    modifier = Modifier.size(32.dp).shadow(4.dp, CircleShape).clip(CircleShape).background(Color(0xFF00C853)).border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Place, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }
            MarkerComposable(
                state = MarkerState(position = endLatLng),
                title = "Drop-off"
            ) {
                Box(
                    modifier = Modifier.size(32.dp).shadow(4.dp, RoundedCornerShape(8.dp)).clip(RoundedCornerShape(8.dp)).background(Color(0xFFFFB300)).border(2.dp, Color.White, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Flag, contentDescription = null, tint = Color.Black, modifier = Modifier.size(20.dp))
                }
            }
        } else {
            Marker(state = MarkerState(position = startLatLng), title = "Pickup")
            Marker(state = MarkerState(position = endLatLng), title = "Drop-off")
        }

        routePoints?.let { route ->
            Polyline(
                points = route,
                color = if (colors.isLight) Color(0xFF1E2630) else Color(0xFFFFB300),
                width = 12f,
                startCap = RoundCap(),
                endCap = RoundCap(),
                jointType = JointType.ROUND
            )
        } ?: run {
            val mockRoute = generateRoutePoints(startLatLng, endLatLng)
            Polyline(
                points = mockRoute,
                color = if (colors.isLight) Color(0xFF1E2630) else Color(0xFFFFB300),
                width = 12f,
                startCap = RoundCap(),
                endCap = RoundCap()
            )
        }
    }
}
