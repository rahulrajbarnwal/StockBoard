package dev.rrb.stocks.network

import dev.rrb.stocks.models.ApiResponse


expect object ConfigLoader {
    suspend fun loadConfig(): ApiResponse?
}