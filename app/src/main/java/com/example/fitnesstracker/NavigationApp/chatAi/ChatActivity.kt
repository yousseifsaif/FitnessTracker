package com.example.fitnesstracker.NavigationApp.chatAi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.fitnesstracker.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var deepSeekApi: DeepSeekApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Retrofit with the correct base URL
        deepSeekApi = Retrofit.Builder()
            .baseUrl("https://api.deepseek.com/")  // أو "https://api.deepseek.com/v1/"
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DeepSeekApiService::class.java)

        binding.btnSend.setOnClickListener {
            val userMessage = binding.editTextUserInput.text.toString()
            if (userMessage.isNotEmpty()) {
                fetchDeepSeekResponse(userMessage)
            }
        }
    }

    private fun fetchDeepSeekResponse(userInput: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val request = DeepSeekRequest(
                    model = "deepseek-v3",  // تحديد النموذج المستخدم
                    messages = listOf(
                        Message(role = "user", content = userInput)
                    ),
                    stream = false  // تحديد إذا كنت تريد رداً متدفقاً أم لا
                )

                val response = deepSeekApi.getDeepSeekResponse(
                    request = request,
                    apiKey = "Bearer sk-4554e455e593497493702773cc21dfe1"  // استبدلها بمفتاحك الفعلي
                )

                withContext(Dispatchers.Main) {
                    binding.txtAIResponse.text = response.choices[0].message.content
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.txtAIResponse.text = "خطأ: ${e.message ?: "حدث خطأ غير معروف"}"
                }
            }
        }
    }
}