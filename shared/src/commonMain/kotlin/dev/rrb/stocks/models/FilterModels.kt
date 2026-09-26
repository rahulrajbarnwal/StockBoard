package dev.rrb.stocks.models

import kotlinx.serialization.Serializable

@Serializable
data class FilterApiResponse(
    val upd: String? = "",
    val keyIndices: FilterCategory? = null,
    val sectoralIndices: FilterCategory? = null,
    val otherIndices: FilterCategory? = null,
    val all: AllStocksCategory? = null,
    val marketcap: FilterCategory? = null
)

@Serializable
data class FilterCategory(
    val filterType: String? = "",
    val paramKey: String? = "",
    val name: String? = "",
    val nse: List<FilterOption>? = emptyList(),
    val bse: List<FilterOption>? = emptyList()
)

@Serializable
data class AllStocksCategory(
    val name: String? = ""
)

@Serializable
data class FilterOption(
    val name: String? = "",
    val indexId: String? = "",
    val exchange: String = "NSE"
)
