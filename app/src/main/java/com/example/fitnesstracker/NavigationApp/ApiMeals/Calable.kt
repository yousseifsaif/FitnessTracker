package com.example.fitnesstracker.NavigationApp.ApiMeals

import com.example.fitnesstracker.NavigationApp.apiWorkouts.Exercise
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface CallableApi {
    @GET("/api/json/v1/1/filter.php")
    fun getCategory(@Query("c") category: String): Call<ApiMealsResponse>
}

data class ApiMealsResponse(
    val meals: List<ApiMeals>
)

data class ApiMeals(
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String
)