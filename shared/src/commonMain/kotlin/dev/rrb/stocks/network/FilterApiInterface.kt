package dev.rrb.stocks.network

import dev.rrb.stocks.models.FilterApiResponse
import dev.rrb.stocks.utils.Logger
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.CancellationException

interface FilterApiInterface {
    suspend fun getFilterOptions(): FilterApiResponse?
}

object FilterApiImpl : FilterApiInterface {
    override suspend fun getFilterOptions(): FilterApiResponse? {
        return try {
            Logger.debug("FilterApiImpl", "Fetching filter options...")

            val response: FilterApiResponse = KtorClient.client.get(Constants.BASE_URL_1 + Constants.FILTER_API).body()
            Logger.debug("FilterApiImpl", "Key Indices: ${response.keyIndices?.nse?.size ?: 0} NSE, ${response.keyIndices?.bse?.size ?: 0} BSE")
            Logger.debug("FilterApiImpl", "Sectoral Indices: ${response.sectoralIndices?.nse?.size ?: 0} NSE, ${response.sectoralIndices?.bse?.size ?: 0} BSE")
            Logger.debug("FilterApiImpl", "Other Indices: ${response.otherIndices?.nse?.size ?: 0} NSE, ${response.otherIndices?.bse?.size ?: 0} BSE")
            Logger.debug("FilterApiImpl", "Market Cap: ${response.marketcap?.nse?.size ?: 0} NSE, ${response.marketcap?.bse?.size ?: 0} BSE")
            Logger.debug("FilterApiImpl", "All Stocks: ${response.all?.name}")

            response
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.error("FilterApiImpl", "Filter API Error: ${e.message}", e)
            null
        }
    }
}
