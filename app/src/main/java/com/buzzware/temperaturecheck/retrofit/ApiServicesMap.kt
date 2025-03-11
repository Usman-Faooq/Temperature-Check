package com.buzzware.temperaturecheck.retrofit


import com.buzzware.temperaturecheck.model.GooglePlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServicesMap {

    @GET("textsearch/json")
    fun getNearbyPlaces(
        //@Query("radius") radius: Int,
        @Query("location") location: String,
        @Query("query") type: String,
        @Query("key") apiKey: String
    ): Call<GooglePlaceResponse>

}