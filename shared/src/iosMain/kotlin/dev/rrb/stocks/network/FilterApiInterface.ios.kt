package dev.rrb.stocks.network

import dev.rrb.stocks.models.FilterApiResponse

actual interface FilterApiInterface {
    actual suspend fun getFilterOptions(): FilterApiResponse?
}

actual object FilterApiImpl : FilterApiInterface {
    actual override suspend fun getFilterOptions(): FilterApiResponse? {
        // iOS fallback
        return null
    }
}