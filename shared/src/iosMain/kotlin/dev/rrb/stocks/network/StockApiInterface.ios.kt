package dev.rrb.stocks.network

import dev.rrb.stocks.models.DefaultPostData
import dev.rrb.stocks.models.StockApiResponse


actual interface StockApiInterface {
    actual suspend fun getStocks(
        apiType: String,
        postData: DefaultPostData
    ): StockApiResponse
}

actual object StockApiImpl : StockApiInterface {
    actual override suspend fun getStocks(
        apiType: String,
        postData: DefaultPostData
    ): StockApiResponse {
        // iOS fallback - return empty for now
        return StockApiResponse(null)
    }
}