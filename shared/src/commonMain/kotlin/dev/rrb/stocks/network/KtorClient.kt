package dev.rrb.stocks.network

import dev.rrb.stocks.utils.Logger
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.Logger as KtorLogger
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

expect fun httpClientEngine(): HttpClientEngine


object KtorClient {

    val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
        explicitNulls = false
    }

    val client: HttpClient by lazy {
        HttpClient(httpClientEngine()) {
            install(ContentNegotiation) {
                json(json)
                // ET feed endpoints often send JSON with a text/html or text/plain header
                json(json, ContentType.Text.Html)
                json(json, ContentType.Text.Plain)
            }
            install(Logging) {
                logger = object : KtorLogger {
                    override fun log(message: String) = Logger.debug("Ktor", message)
                }
                level = LogLevel.BODY
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 30_000
                connectTimeoutMillis = 15_000
            }
        }
    }
}