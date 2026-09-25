package dev.rrb.stocks.network

import android.content.Context
import dev.rrb.stocks.models.ApiResponse
import kotlinx.serialization.json.Json

actual object ConfigLoader {

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private var context: Context? = null

    fun initialize(ctx: Context) {
        context = ctx
    }

    actual suspend fun loadConfig(): ApiResponse? {
        return try {
            val inputStream = context?.assets?.open("config/today_data.json")
            val jsonString = inputStream?.bufferedReader().use { reader ->
                reader?.readText() ?: ""
            }
            json.decodeFromString<ApiResponse>(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}