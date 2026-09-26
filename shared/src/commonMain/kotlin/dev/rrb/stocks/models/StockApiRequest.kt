package dev.rrb.stocks.models

import kotlinx.serialization.Serializable

@Serializable
data class StockApiRequest(
    val apiType: String,
    val pagesize: String,
    val pageNumber: String,
    val duration: String,
    val viewId: String,
    val filterValue: List<String>,
    val filterType: String
)
