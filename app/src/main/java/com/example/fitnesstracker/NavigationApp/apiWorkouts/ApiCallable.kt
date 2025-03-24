package com.example.fitnesstracker.NavigationApp.apiWorkouts

import retrofit2.Call
import retrofit2.http.GET

interface ApiCallable {
    @GET("/exercises/bodyPart/back?limit=100&offset=0&rapidapi-key=5747619d4fmsha962e3fb43f4c2bp1f7a0djsn36bbdd3ba9c9")
    fun getExercise(): Call<List<Exercise>>
}