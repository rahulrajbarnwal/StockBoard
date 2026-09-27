package dev.rrb.stocks.network

import dev.rrb.stocks.models.ApiResponse

actual object ConfigLoader {
    actual suspend fun loadConfig(): ApiResponse? {
        // iOS fallback - no bundled config yet
        return null
    }
}

/*
import dev.rrb.stocks.models.ApiResponse
import kotlinx.serialization.json.Json
import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding

actual object ConfigLoader {

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    actual suspend fun loadConfig(): ApiResponse? {
        return try {
            // For iOS, read from bundle resources
            val filePath = NSBundle.mainBundle.pathForResource("today_data", "json")
            if (filePath != null) {
                val jsonString = NSString(
                    contentsOfFile = filePath,
                    encoding = NSUTF8StringEncoding
                ) as String
                json.decodeFromString<ApiResponse>(jsonString)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
* */