package dev.rrb.stocks.network

import dev.rrb.stocks.models.ApiResponse
import dev.rrb.stocks.models.StockData
import android.util.Log
import dev.rrb.stocks.utils.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

actual object ApiService {

    private var configData: ApiResponse? = null

    // ✅ StateFlow for API responses
    private val _apiResponseFlow = MutableStateFlow<dev.rrb.stocks.network.ApiResponse>(
        dev.rrb.stocks.network.ApiResponse.Loading
    )

    actual fun getApiResponseFlow(): StateFlow<dev.rrb.stocks.network.ApiResponse> {
        return _apiResponseFlow
    }

    actual suspend fun loadConfigData(): ApiResponse? {
        configData = ConfigLoader.loadConfig()
        Log.d("ApiService", "Config loaded: ${configData != null}")
        return configData
    }

    actual fun getConfigData(): ApiResponse? {
        return configData
    }

    actual fun generateSampleStocks(apiType: String): List<StockData> {
        return emptyList()
    }

    actual suspend fun fetchStocksFromApi(
        apiType: String,
        filterType: String,
        filterId: String
    ): List<StockData> {
        return try {
            _apiResponseFlow.value = dev.rrb.stocks.network.ApiResponse.Loading  // ✅ Emit Loading
            Log.d("ApiService", "=== Fetching stocks for: $apiType (filter: $filterType/$filterId) ===")

            val tab = configData?.tabs?.find { it.apiType == apiType }
            Log.d("ApiService", "Tab found: ${tab?.nm ?: "NOT FOUND"}")

            if (tab != null && configData != null) {
                val defaultData = configData!!.defaultPostData

                if (defaultData != null) {
                    val updatedPostData = defaultData.copy(
                        filterValue = listOf(filterId),
                        filterType = filterType
                    )

                    Log.d("ApiService", "Using PostData: filterType=$filterType, filterId=$filterId")

                    try {
                        Log.d("ApiService", "Calling API for: ${tab.apiType}")
                        val response = StockApiImpl.getStocks(
                            apiType = tab.apiType,
                            postData = updatedPostData
                        )

                        Log.d("ApiService", "API Response received")
                        Log.d("ApiService", "DataList size: ${response.dataList?.size ?: 0}")

                        if (response.dataList != null) {
                            val stocks = parseApiResponse(response)
                            Log.d("ApiService", "Parsed ${stocks.size} stocks")
                            _apiResponseFlow.value = dev.rrb.stocks.network.ApiResponse.Success(stocks)  // ✅ Emit Success
                            stocks
                        } else {
                            Log.w("ApiService", "DataList is null")
                            _apiResponseFlow.value = dev.rrb.stocks.network.ApiResponse.Error("No data received")  // ✅ Emit Error
                            emptyList()
                        }

                    } catch (apiException: Exception) {
                        Log.e("ApiService", "API call FAILED", apiException)
                        apiException.printStackTrace()
                        val errorMsg = apiException.message ?: "API Error"
                        _apiResponseFlow.value = dev.rrb.stocks.network.ApiResponse.Error(errorMsg)  // ✅ Emit Error
                        emptyList()
                    }
                } else {
                    Log.w("ApiService", "DefaultPostData is null")
                    _apiResponseFlow.value = dev.rrb.stocks.network.ApiResponse.Error("Config error")  // ✅ Emit Error
                    emptyList()
                }
            } else {
                Log.w("ApiService", "No tab found or config is null")
                _apiResponseFlow.value = dev.rrb.stocks.network.ApiResponse.Error("Tab not found")  // ✅ Emit Error
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("ApiService", "fetchStocksFromApi error", e)
            e.printStackTrace()
            val errorMsg = e.message ?: "Unknown error"
            _apiResponseFlow.value = dev.rrb.stocks.network.ApiResponse.Error(errorMsg)  // ✅ Emit Error
            emptyList()
        }
    }

    private fun parseApiResponse(response: dev.rrb.stocks.models.StockApiResponse): List<StockData> {
        return response.dataList?.mapNotNull { item ->
            try {
                val dataMap = item.data?.associate { it.keyId to it.value } ?: emptyMap()

                Logger.debug("ApiService", "=== Parsing Stock: ${item.assetName} ===")
                Logger.debug("ApiService", "Available keys: ${dataMap.keys.joinToString(", ")}")

                val companyName = dataMap["shortName"] ?: item.assetName ?: ""
                Logger.debug("ApiService", "Company Name: $companyName")

                // ✅ Check all possible price fields
                val lastTradedPrice = dataMap["lastTradedPrice"]
                    ?: dataMap["ltp"]
                    ?: dataMap["price"]
                    ?: "0"
                Logger.debug("ApiService", "lastTradedPrice field value: $lastTradedPrice")

                val currentPrice = lastTradedPrice.toNumberOrZero()
                Logger.debug("ApiService", "Parsed currentPrice: $currentPrice")

                val priceChange = (dataMap["netChange"] ?: dataMap["change"] ?: "0").toNumberOrZero()
                Logger.debug("ApiService", "priceChange: $priceChange")

                val percentChangeStr = (dataMap["percentChange"] ?: dataMap["pctChange"] ?: "0")
                    .replace("%", "")
                    .replace(",", "")
                    .trim()
                Logger.debug("ApiService", "percentChangeStr: $percentChangeStr")
                val percentChange = percentChangeStr.toDoubleOrNull() ?: 0.0

                val volume = dataMap["volume"] ?: "0"
                Logger.debug("ApiService", "volume: $volume")

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
                Log.e("ApiService", "Error mapping stock: ${e.message}", e)
                null
            }
        } ?: emptyList()
    }

    // API sends numbers with thousand separators (e.g. "2,832"), which toDoubleOrNull() rejects
    private fun String.toNumberOrZero(): Double = replace(",", "").trim().toDoubleOrNull() ?: 0.0
}
