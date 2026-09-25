package dev.rrb.stocks.network

import dev.rrb.stocks.models.PostData
import dev.rrb.stocks.models.StockApiResponse


expect interface StockApiInterface {
    suspend fun getStocks(
        postData: PostData
    ): StockApiResponse
}

expect object StockApiImpl : StockApiInterface {
    override suspend fun getStocks(
        postData: PostData
    ): StockApiResponse
}