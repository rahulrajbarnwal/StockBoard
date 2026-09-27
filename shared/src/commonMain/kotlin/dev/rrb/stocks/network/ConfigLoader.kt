package dev.rrb.stocks.network

import dev.rrb.stocks.models.ApiResponse
import dev.rrb.stocks.utils.Logger
import kotlinx.coroutines.CancellationException
import org.jetbrains.compose.resources.ExperimentalResourceApi
import stocks.shared.generated.resources.Res

object ConfigLoader {

    // Bundled via composeResources/files so it ships on both Android and iOS
    private const val CONFIG_PATH = "files/config/today_data.json"

    @OptIn(ExperimentalResourceApi::class)
    suspend fun loadConfig(): ApiResponse? {
        return try {
            val jsonString = Res.readBytes(CONFIG_PATH).decodeToString()
            KtorClient.json.decodeFromString<ApiResponse>(jsonString)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.error("ConfigLoader", "Failed to load config: ${e.message}", e)
            null
        }
    }
}
