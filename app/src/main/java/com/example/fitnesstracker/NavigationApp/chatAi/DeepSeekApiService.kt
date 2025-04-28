package com.example.fitnesstracker.NavigationApp.chatAi

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface DeepSeekApiService {
    @Headers(
        "Content-Type: application/json",
        "Accept: application/json"
    )
    @POST("chat/completions")

    suspend fun getDeepSeekResponse(
        @Body request: DeepSeekRequest,
        @Header("Authorization") apiKey: String = "Bearer sk-4554e455e593497493702773cc21dfe1" // استبدل بالمفتاح الفعلي
    ): DeepSeekResponse

    companion object {
        private const val BASE_URL = "https://api.deepseek.com/"

        fun create(): DeepSeekApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DeepSeekApiService::class.java)
        }
    }
}