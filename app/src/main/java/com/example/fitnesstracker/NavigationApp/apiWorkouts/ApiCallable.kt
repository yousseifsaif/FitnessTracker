package com.example.fitnesstracker.NavigationApp.apiWorkouts

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ApiCallable {
    @Headers(
        "X-RapidAPI-Key: dc3ee3ec14msh9ab4e174841d3ebp13d639jsn9e2f10c78de4",
        "X-RapidAPI-Host: exercisedb.p.rapidapi.com"
    )
    @GET("exercises/bodyPart/{bodyPart}?limit=100&offset=20")
    fun getExercises(@Path("bodyPart") bodyPart: String): Call<List<Exercise>>
}