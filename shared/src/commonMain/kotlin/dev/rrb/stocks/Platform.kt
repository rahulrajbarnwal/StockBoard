package dev.rrb.stocks

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform