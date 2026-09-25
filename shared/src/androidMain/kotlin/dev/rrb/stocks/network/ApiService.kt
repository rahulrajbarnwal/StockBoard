package dev.rrb.stocks.network

import android.util.Log
import dev.rrb.stocks.models.ApiResponse
import dev.rrb.stocks.models.StockData

actual object ApiService {

    private var configData: ApiResponse? = null

    actual suspend fun loadConfigData(): ApiResponse? {
        configData = ConfigLoader.loadConfig()
        Log.d("ApiService", "Config loaded: ${configData != null}")
        return configData
    }

    actual fun getConfigData(): ApiResponse? {
        return configData
    }

    actual fun generateSampleStocks(apiType: String): List<StockData> {
        return emptyList() // No longer using sample data
    }

    actual suspend fun fetchStocksFromApi(apiType: String): List<StockData> {
        return try {
            Log.d("ApiService", "=== Fetching stocks for: $apiType ===")

            val tab = configData?.tabs?.find { it.postData.apiType == apiType }
            Log.d("ApiService", "Tab found: ${tab?.nm ?: "NOT FOUND"}")

            if (tab != null) {
                val postData = tab.postData
                Log.d("ApiService", "PostData - apiType: ${postData.apiType}, pagesize: ${postData.pagesize}")

                try {
                    Log.d("ApiService", "Calling API...")
                    val response = StockApiImpl.getStocks(
                        postData = postData
                    )

                    Log.d("ApiService", "API Response received")
                    Log.d("ApiService", "DataList size: ${response.dataList?.size ?: 0}")

                    if (response.dataList != null) {
                        val stocks = response.dataList.mapNotNull { item ->
                            try {
                                // Extract values from the data array
                                val dataMap = item.data?.associate { it.keyId to it.value } ?: emptyMap()

                                val companyName = dataMap["shortName"] ?: item.assetName ?: ""
                                val currentPrice = dataMap["lastTradedPrice"]?.toDoubleOrNull() ?: 0.0
                                val priceChange = dataMap["netChange"]?.toDoubleOrNull() ?: 0.0
                                val percentChangeStr = dataMap["percentChange"]?.replace("%", "")?.trim() ?: "0"
                                val percentChange = percentChangeStr.toDoubleOrNull() ?: 0.0
                                val volume = dataMap["volume"] ?: "0"

                                Log.d("ApiService", "Stock: $companyName - $currentPrice - $percentChange%")

                                StockData(
                                    id = item.assetId ?: "",
                                    companyName = companyName,
                                    symbol = item.assetSymbol ?: "",
                                    currentPrice = currentPrice,
                                    priceChange = priceChange,
                                    changePercent = percentChange,
                                    volume = volume,
                                    exchange = if (item.assetExchangeId == "50") "NSE" else "BSE"
                                )
                            } catch (e: Exception) {
                                Log.e("ApiService", "Error mapping stock: ${e.message}")
                                null
                            }
                        }

                        Log.d("ApiService", "Returning ${stocks.size} stocks")
                        stocks
                    } else {
                        Log.w("ApiService", "DataList is null")
                        emptyList()
                    }

                } catch (apiException: Exception) {
                    Log.e("ApiService", "API call FAILED", apiException)
                    apiException.printStackTrace()
                    emptyList()
                }
            } else {
                Log.w("ApiService", "No tab found for apiType: $apiType")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("ApiService", "fetchStocksFromApi error", e)
            e.printStackTrace()
            emptyList()
        }
    }
}