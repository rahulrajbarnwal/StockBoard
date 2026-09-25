package dev.rrb.stocks.models

import kotlinx.serialization.Serializable

@Serializable
data class StockData(
    val id: String,
    val companyName: String,
    val symbol: String,
    val currentPrice: Double,
    val priceChange: Double,
    val changePercent: Double,
    val volume: String,
    val exchange: String
)

@Serializable
data class DashboardCategory(
    val id: String,
    val name: String,
    val viewAllText: String,
    val apiType: String,
    val stocks: List<StockData> = emptyList()
)

@Serializable
data class ApiResponse(
    val data_url: String,
    val tabs: List<TabData>,
    val filter: FilterData
)

@Serializable
data class TabData(
    val nm: String,
    val va: String,
    val postData: PostData
)

@Serializable
data class PostData(
    val apiType: String,
    val pagesize: String,
    val pageNumber: String,
    val duration: String,
    val viewId: String
)

@Serializable
data class FilterData(
    val filterUrl: String,
    val fu_upd: String,
    val dinId: String,
    val dinName: String,
    val dParamKey: String
)