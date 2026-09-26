package dev.rrb.stocks.network

import dev.rrb.stocks.models.DefaultPostData
import dev.rrb.stocks.models.StockApiResponse


expect interface StockApiInterface {
    suspend fun getStocks(
        apiType: String,
        postData: DefaultPostData
    ): StockApiResponse
}

expect object StockApiImpl : StockApiInterface {
    override suspend fun getStocks(
        apiType: String,
        postData: DefaultPostData
    ): StockApiResponse
}