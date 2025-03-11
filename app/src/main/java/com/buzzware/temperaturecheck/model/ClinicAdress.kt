package com.buzzware.temperaturecheck.model

data class ClinicAdress (
    val place_id : String = "",
    val name : String = "",
    val vicinity : String = "",
    val location : Location = Location(),
)