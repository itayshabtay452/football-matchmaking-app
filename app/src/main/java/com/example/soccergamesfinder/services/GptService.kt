package com.example.soccergamesfinder.services

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

interface GptService {
    suspend fun sendPrompt(prompt: String): String
}

@Singleton
class GptServiceImpl @Inject constructor(
    private val apiKey: String
) : GptService {

    private val client = OkHttpClient()

    override suspend fun sendPrompt(prompt: String): String = withContext(Dispatchers.IO) {
        val url = "https://openrouter.ai/api/v1/chat/completions"
        val mediaType = "application/json".toMediaType()

        val requestBody = JSONObject().apply {
            put("model", "openai/gpt-3.5-turbo")
            put("messages", JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "user")
                    put("content", prompt)
                })
            })
        }.toString().toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("HTTP-Referer", "https://yourapp.com") // ← חובה ל-OpenRouter
            .build()

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    Log.e("GPT", "❌ failed to contact GPT: $response")
                    throw IOException("Unexpected code $response")
                }

                val body = response.body?.string() ?: throw IOException("Empty response")
                Log.d("GPT", "🧠 Response body: $body")

                val json = JSONObject(body)

                if (!json.has("choices")) {
                    Log.e("GPT", "❌ No 'choices' field in response: $json")
                    throw IOException("Missing 'choices' in response")
                }

                val choices = json.getJSONArray("choices")
                val message = choices.getJSONObject(0).getJSONObject("message")
                return@withContext message.getString("content")
            }
        } catch (e: Exception) {
            Log.e("GPT", "Exception: ${e.message}", e)
            throw e
        }
    }
}
