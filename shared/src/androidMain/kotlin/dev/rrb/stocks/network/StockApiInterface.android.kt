package dev.rrb.stocks.network

import android.util.Log
import dev.rrb.stocks.models.DefaultPostData
import dev.rrb.stocks.models.StockApiRequest
import dev.rrb.stocks.models.StockApiResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitStockApi {
    @POST(Constants.STOCK_API_REQUEST)
    suspend fun getStocks(
        @Body request : StockApiRequest
    ): StockApiResponse
}

object RetrofitClient {
    private val loggingInterceptor = HttpLoggingInterceptor { message ->
        Log.d("OkHttp", message)
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://etapi.indiatimes.com/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: RetrofitStockApi = retrofit.create(RetrofitStockApi::class.java)
}

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
        return try {
            Log.d("StockApiImpl", "Creating request - apiType: $apiType")
            val request = StockApiRequest(
                apiType = apiType,
                pagesize = postData.pagesize,
                pageNumber = postData.pageNumber,
                duration = postData.duration,
                viewId = postData.viewId,
                filterValue = postData.filterValue,
                filterType = postData.filterType
            )

            Log.d("StockApiImpl", "Request: $request")
            val response = RetrofitClient.api.getStocks(request)
            Log.d("StockApiImpl", "Response: ${response.dataList?.size ?: 0} items")
            response
        } catch (e: Exception) {
            Log.e("StockApiImpl", "API Error: ${e.message}", e)
            e.printStackTrace()
            StockApiResponse(null)
        }
    }
}