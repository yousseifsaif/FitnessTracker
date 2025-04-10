package com.example.fitnesstracker.NavigationApp.ApiMeals



import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Calable {
    @GET("api/recipes/v2")
    fun searchRecipes(
        @Query("type") type: String = "public",
        @Query("q") query: String,
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String
    ): Call<EdamamResponse>

}
