package dev.rrb.stocks.network

import dev.rrb.stocks.models.ApiResponse
import dev.rrb.stocks.models.StockData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

actual object ApiService {

    private val _apiResponseFlow = MutableStateFlow<dev.rrb.stocks.network.ApiResponse>(
        dev.rrb.stocks.network.ApiResponse.Loading
    )

    actual fun getApiResponseFlow(): StateFlow<dev.rrb.stocks.network.ApiResponse> {
        return _apiResponseFlow
    }

    actual suspend fun loadConfigData(): ApiResponse? {
        return null
    }

    actual fun getConfigData(): ApiResponse? {
        return null
    }

    actual fun generateSampleStocks(apiType: String): List<StockData> {
        return emptyList()
    }

    actual suspend fun fetchStocksFromApi(
        apiType: String,
        filterType: String,
        filterId: String
    ): List<StockData> {
        _apiResponseFlow.value = dev.rrb.stocks.network.ApiResponse.Loading
        return emptyList()
    }
}