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
    val data_url: String? = "",
    val defaultPostData: DefaultPostData? = null,  // ✅ NEW
    val tabs: List<TabData> = emptyList(),
    val filter: FilterData? = null
)

@Serializable
data class DefaultPostData(
    val pagesize: String = "6",
    val pageNumber: String = "1",
    val duration: String = "1D",
    val viewId: String = "15070",
    val filterValue: List<String> = listOf("2371"),
    val filterType: String = "index"
)

@Serializable
data class TabData(
    val nm: String = "",
    val va: String = "",
    val apiType: String = ""  // ✅ SIMPLIFIED
)

@Serializable
data class FilterData(
    val filterUrl: String? = "",
    val fu_upd: String? = "",
    val dinId: String? = "",
    val dinName: String? = "",
    val dParamKey: String? = ""
)