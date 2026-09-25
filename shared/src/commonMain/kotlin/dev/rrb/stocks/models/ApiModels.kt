package dev.rrb.stocks.models

import kotlinx.serialization.Serializable

@Serializable
data class StockApiResponse(
    val dataList: List<StockItem>? = emptyList(),
    val pageSummary: PageSummary? = null
)

@Serializable
data class StockItem(
    val assetName: String? = "",
    val assetSymbol: String? = "",
    val assetId: String? = "",
    val assetSeoName: String? = "",
    val assetType: String? = "",
    val assetExchangeId: String? = "",
    val assetScripCode: String? = "",
    val data: List<DataField>? = emptyList()
)

@Serializable
data class DataField(
    val keyId: String? = "",
    val keyText: String? = "",
    val value: String? = "",
    val trend: String? = "",
    val valueType: String? = "text"
)

@Serializable
data class PageSummary(
    val totalRecords: Int? = 0,
    val totalpages: Int? = 0,
    val pagesize: Int? = 0,
    val pageno: Int? = 1
)