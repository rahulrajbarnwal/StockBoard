package dev.rrb.stocks.navigation

enum class Screen {
    HOME,
    WATCHLIST,
    PORTFOLIO,
    PROFILE
}

sealed class NavigationEvent {
    data class NavigateTo(val screen: Screen) : NavigationEvent()
}