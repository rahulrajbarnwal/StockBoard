package dev.rrb.stocks.network

import dev.rrb.stocks.models.ApiResponse
import dev.rrb.stocks.models.StockData

actual object ApiService {

    private var configData: ApiResponse? = null

    actual suspend fun loadConfigData(): ApiResponse? {
        configData = ConfigLoader.loadConfig()
        return configData
    }

    actual fun getConfigData(): ApiResponse? {
        return configData
    }

    actual fun generateSampleStocks(apiType: String): List<StockData> {
        return when (apiType) {
            Constants.API_TYPE_GAINERS -> listOf(
                StockData(
                    id = "1",
                    companyName = "Mankind Pharma",
                    symbol = "MANKIND",
                    currentPrice = 2438.00,
                    priceChange = 137.00,
                    changePercent = 5.96,
                    volume = "1.3M",
                    exchange = "NSE"
                )
            )
            else -> emptyList()
        }
    }

    actual suspend fun fetchStocksFromApi(apiType: String): List<StockData> {
        // iOS fallback - use sample data
        return generateSampleStocks(apiType)
    }
}