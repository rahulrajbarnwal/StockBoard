package dev.rrb.stocks.network

import dev.rrb.stocks.models.ApiResponse
import dev.rrb.stocks.models.StockData
import kotlinx.coroutines.flow.StateFlow


expect object ApiService {
    fun getApiResponseFlow(): StateFlow<dev.rrb.stocks.network.ApiResponse>
    suspend fun loadConfigData(): ApiResponse?
    fun getConfigData(): ApiResponse?
    fun generateSampleStocks(apiType: String): List<StockData>
    suspend fun fetchStocksFromApi(
        apiType: String,
        filterType: String,
        filterId: String
    ): List<StockData>

}