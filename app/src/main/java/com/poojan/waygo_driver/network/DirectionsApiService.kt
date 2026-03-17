package com.poojan.waygo_driver.network

import com.google.android.gms.maps.model.LatLng
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface DirectionsApiService {
    @GET("directions/json")
    suspend fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") apiKey: String
    ): DirectionsResponse
}

data class DirectionsResponse(
    val routes: List<Route>
)

data class Route(
    val overview_polyline: OverviewPolyline,
    val legs: List<Leg>
)

data class Leg(
    val distance: Distance,
    val duration: Duration
)

data class Distance(val text: String, val value: Int)
data class Duration(val text: String, val value: Int)

data class OverviewPolyline(
    val points: String
)

object NetworkClient {
    private const val BASE_URL = "https://maps.googleapis.com/maps/api/"

    val directionsApi: DirectionsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DirectionsApiService::class.java)
    }
}
