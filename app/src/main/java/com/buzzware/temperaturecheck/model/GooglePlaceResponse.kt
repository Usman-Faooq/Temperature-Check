package com.buzzware.temperaturecheck.model

data class GooglePlaceResponse(
    val results: List<Place>
)


data class Location(
    val lat: Double = 0.0,
    val lng: Double = 0.0
)

data class Place(
    val name: String ="",
    val place_id: String = "",
    val formatted_address: String ="",
    val geometry: Geometry = Geometry(),
)

data class Geometry(
    val location: Location = Location()
)