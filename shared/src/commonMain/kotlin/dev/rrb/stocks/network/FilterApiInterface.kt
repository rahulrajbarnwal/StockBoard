package dev.rrb.stocks.network

import dev.rrb.stocks.models.FilterApiResponse

expect interface FilterApiInterface {
    suspend fun getFilterOptions(): FilterApiResponse?
}

expect object FilterApiImpl : FilterApiInterface {
    override suspend fun getFilterOptions(): FilterApiResponse?
}