package dev.rrb.stocks.network

import dev.rrb.stocks.models.ApiResponse
import dev.rrb.stocks.models.StockApiResponse
import dev.rrb.stocks.models.StockData
import dev.rrb.stocks.utils.Logger
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object ApiService {

    private var configData: ApiResponse? = null

    private val _apiResponseFlow = MutableStateFlow<dev.rrb.stocks.network.ApiResponse>(
        dev.rrb.stocks.network.ApiResponse.Loading
    )

    fun getApiResponseFlow(): StateFlow<dev.rrb.stocks.network.ApiResponse> {
        return _apiResponseFlow
    }

    suspend fun loadConfigData(): ApiResponse? {
        configData = ConfigLoader.loadConfig()
        Logger.debug("ApiService", "Config loaded: ${configData != null}")
        return configData
    }

    fun getConfigData(): ApiResponse? {
        return configData
    }

    fun generateSampleStocks(apiType: String): List<StockData> {
        return emptyList()
    }

    suspend fun fetchStocksFromApi(
        apiType: String,
        filterType: String,
        filterId: String
    ): List<StockData> {
        _apiResponseFlow.value = dev.rrb.stocks.network.ApiResponse.Loading
        Logger.debug("ApiService", "=== Fetching stocks for: $apiType (filter: $filterType/$filterId) ===")

        val config = configData
        val tab = config?.tabs?.find { it.apiType == apiType }
        Logger.debug("ApiService", "Tab found: ${tab?.nm ?: "NOT FOUND"}")

        if (config == null || tab == null) {
            Logger.info("ApiService", "No tab found or config is null")
            return emitError("Tab not found")
        }

        val defaultData = config.defaultPostData
        if (defaultData == null) {
            Logger.info("ApiService", "DefaultPostData is null")
            return emitError("Config error")
        }

        val updatedPostData = defaultData.copy(
            filterValue = listOf(filterId),
            filterType = filterType
        )
        Logger.debug("ApiService", "Using PostData: filterType=$filterType, filterId=$filterId")

        return try {
            Logger.debug("ApiService", "Calling API for: ${tab.apiType}")
            val response = StockApiImpl.getStocks(
                apiType = tab.apiType,
                postData = updatedPostData
            )
            Logger.debug("ApiService", "DataList size: ${response.dataList?.size ?: 0}")

            if (response.dataList != null) {
                val stocks = parseApiResponse(response)
                Logger.debug("ApiService", "Parsed ${stocks.size} stocks")
                _apiResponseFlow.value = dev.rrb.stocks.network.ApiResponse.Success(stocks)
                stocks
            } else {
                Logger.info("ApiService", "DataList is null")
                emitError("No data received")
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.error("ApiService", "API call FAILED", e)
            emitError(e.message ?: "API Error")
        }
    }

    private fun emitError(message: String): List<StockData> {
        _apiResponseFlow.value = dev.rrb.stocks.network.ApiResponse.Error(message)
        return emptyList()
    }

    private fun parseApiResponse(response: StockApiResponse): List<StockData> {
        return response.dataList?.mapNotNull { item ->
            try {
                val dataMap = item.data?.associate { it.keyId to it.value } ?: emptyMap()

                Logger.debug("ApiService", "=== Parsing Stock: ${item.assetName} ===")
                Logger.debug("ApiService", "Available keys: ${dataMap.keys.joinToString(", ")}")

                val companyName = dataMap["shortName"] ?: item.assetName ?: ""

                val lastTradedPrice = dataMap["lastTradedPrice"]
                    ?: dataMap["ltp"]
                    ?: dataMap["price"]
                    ?: "0"
                val currentPrice = lastTradedPrice.toNumberOrZero()

                val priceChange = (dataMap["netChange"] ?: dataMap["change"] ?: "0").toNumberOrZero()

                val percentChangeStr = (dataMap["percentChange"] ?: dataMap["pctChange"] ?: "0")
                    .replace("%", "")
                    .replace(",", "")
                    .trim()
                val percentChange = percentChangeStr.toDoubleOrNull() ?: 0.0

                val volume = dataMap["volume"] ?: "0"

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
                Logger.error("ApiService", "Error mapping stock: ${e.message}", e)
                null
            }
        } ?: emptyList()
    }

    // API sends numbers with thousand separators (e.g. "2,832"), which toDoubleOrNull() rejects
    private fun String.toNumberOrZero(): Double = replace(",", "").trim().toDoubleOrNull() ?: 0.0
}
