package com.example.fitnesstracker.NavigationApp.apiWorkouts

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiCallable {
    @Headers(
        "X-RapidAPI-Key: 5747619d4fmsha962e3fb43f4c2bp1f7a0djsn36bbdd3ba9c9",  // تأكد من وضع مفتاح API الصحيح هنا
        "X-RapidAPI-Host: exercisedb.p.rapidapi.com"
    )
    @GET("exercises/bodyPart/{bodyPart}?limit=100&offset=20")
    fun getExercises(@Path("bodyPart") bodyPart: String) : Call<List<Exercise>>
}