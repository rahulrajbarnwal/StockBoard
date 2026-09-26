package dev.rrb.stocks.network

import dev.rrb.stocks.models.StockData
import kotlinx.serialization.Serializable

sealed class ApiResponse {
    data class Success(val stocks: List<StockData>) : ApiResponse()
    data class Error(val message: String) : ApiResponse()
    object Loading : ApiResponse()
}