package dev.rrb.stocks.network

import dev.rrb.stocks.models.PostData
import dev.rrb.stocks.models.StockApiResponse


actual interface StockApiInterface {
    actual suspend fun getStocks(
        postData: PostData
    ): StockApiResponse
}

actual object StockApiImpl : StockApiInterface {
    actual override suspend fun getStocks(
        postData: PostData
    ): StockApiResponse {
        // iOS fallback - return empty for now
        return StockApiResponse(null)
    }
}