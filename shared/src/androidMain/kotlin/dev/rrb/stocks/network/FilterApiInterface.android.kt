package dev.rrb.stocks.network

import dev.rrb.stocks.models.FilterApiResponse
import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

interface RetrofitFilterApi {
    @GET(Constants.FILTER_API)
    suspend fun getFilterOptions(): FilterApiResponse?
}

object FilterRetrofitClient {
    private val loggingInterceptor = HttpLoggingInterceptor { message ->
        Log.d("OkHttp-Filter", message)
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL_1)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: RetrofitFilterApi = retrofit.create(RetrofitFilterApi::class.java)
}

actual interface FilterApiInterface {
    actual suspend fun getFilterOptions(): FilterApiResponse?
}

actual object FilterApiImpl : FilterApiInterface {
    actual override suspend fun getFilterOptions(): FilterApiResponse? {
        return try {
            Log.d("FilterApiImpl", "🚀 Fetching filter options...")

            val response = FilterRetrofitClient.api.getFilterOptions()
            Log.d("FilterApiImpl", "✅ Response received: $response")
            Log.d("FilterApiImpl", "✅ Key Indices: ${response?.keyIndices?.nse?.size ?: 0} NSE, ${response?.keyIndices?.bse?.size ?: 0} BSE")
            Log.d("FilterApiImpl", "✅ Sectoral Indices: ${response?.sectoralIndices?.nse?.size ?: 0} NSE, ${response?.sectoralIndices?.bse?.size ?: 0} BSE")
            Log.d("FilterApiImpl", "✅ Other Indices: ${response?.otherIndices?.nse?.size ?: 0} NSE, ${response?.otherIndices?.bse?.size ?: 0} BSE")
            Log.d("FilterApiImpl", "✅ Market Cap: ${response?.marketcap?.nse?.size ?: 0} NSE, ${response?.marketcap?.bse?.size ?: 0} BSE")
            Log.d("FilterApiImpl", "✅ All Stocks: ${response?.all?.name}")

            response
        } catch (e: Exception) {
            Log.e("FilterApiImpl", "❌ Filter API Error: ${e.message}", e)
            e.printStackTrace()
            null
        }
    }
}