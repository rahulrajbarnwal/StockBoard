package dev.rrb.stocks.network

import dev.rrb.stocks.models.DefaultPostData
import dev.rrb.stocks.models.StockApiRequest
import dev.rrb.stocks.models.StockApiResponse
import dev.rrb.stocks.utils.Logger
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.CancellationException

interface StockApiInterface {
    suspend fun getStocks(
        apiType: String,
        postData: DefaultPostData
    ): StockApiResponse
}

object StockApiImpl : StockApiInterface {
    override suspend fun getStocks(
        apiType: String,
        postData: DefaultPostData
    ): StockApiResponse {
        return try {
            Logger.debug("StockApiImpl", "Creating request - apiType: $apiType")
            val request = StockApiRequest(
                apiType = apiType,
                pagesize = postData.pagesize,
                pageNumber = postData.pageNumber,
                duration = postData.duration,
                viewId = postData.viewId,
                filterValue = postData.filterValue,
                filterType = postData.filterType
            )

            Logger.debug("StockApiImpl", "Request: $request")
            val response: StockApiResponse = KtorClient.client.post(Constants.BASE_URL_STOCKS + Constants.STOCK_API_REQUEST) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body()
            Logger.debug("StockApiImpl", "Response: ${response.dataList?.size ?: 0} items")
            response
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.error("StockApiImpl", "API Error: ${e.message}", e)
            StockApiResponse(null)
        }
    }
}
