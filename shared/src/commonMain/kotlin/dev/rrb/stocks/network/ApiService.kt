package dev.rrb.stocks.network

import dev.rrb.stocks.models.ApiResponse
import dev.rrb.stocks.models.StockData
import kotlinx.serialization.json.Json


expect object ApiService {
    suspend fun loadConfigData(): ApiResponse?
    fun getConfigData(): ApiResponse?
    fun generateSampleStocks(apiType: String): List<StockData>
    suspend fun fetchStocksFromApi(apiType: String): List<StockData>
}