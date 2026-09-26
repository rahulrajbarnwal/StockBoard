package dev.rrb.stocks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.rrb.stocks.models.DashboardCategory
import dev.rrb.stocks.models.StockData
import dev.rrb.stocks.navigation.Screen
import dev.rrb.stocks.ui.CategoryCard
import dev.rrb.stocks.ui.StocksOverviewCard
import dev.rrb.stocks.ui.StockItem
import dev.rrb.stocks.ui.BottomNavigationBar

// ============================================
// STOCK ITEM PREVIEWS
// ============================================

@Preview(showBackground = true, name = "Stock Item - Positive")
@Composable
fun StockItemPositivePreview() {
    val sampleStock = StockData(
        id = "1",
        companyName = "Mankind Pharma",
        symbol = "MANKIND",
        currentPrice = 2438.00,
        priceChange = 137.00,
        changePercent = 5.96,
        volume = "1.3M",
        exchange = "NSE"
    )

    MaterialTheme {
        StockItem(stock = sampleStock)
    }
}

@Preview(showBackground = true, name = "Stock Item - Negative")
@Composable
fun StockItemNegativePreview() {
    val negativeSampleStock = StockData(
        id = "2",
        companyName = "Declining Corp",
        symbol = "DECLINE",
        currentPrice = 1500.00,
        priceChange = -125.00,
        changePercent = -7.69,
        volume = "2.1M",
        exchange = "NSE"
    )

    MaterialTheme {
        StockItem(stock = negativeSampleStock)
    }
}

// ============================================
// CATEGORY CARD PREVIEW
// ============================================

@Preview(showBackground = true, name = "Category Card - Multiple Stocks")
@Composable
fun CategoryCardPreview() {
    val sampleStocks = listOf(
        StockData(
            id = "1",
            companyName = "Mankind Pharma",
            symbol = "MANKIND",
            currentPrice = 2438.00,
            priceChange = 137.00,
            changePercent = 5.96,
            volume = "1.3M",
            exchange = "NSE"
        ),
        StockData(
            id = "2",
            companyName = "Wockhardt",
            symbol = "WOCKHARDT",
            currentPrice = 2187.00,
            priceChange = 101.00,
            changePercent = 4.86,
            volume = "1.7M",
            exchange = "NSE"
        ),
        StockData(
            id = "3",
            companyName = "Laurus Labs",
            symbol = "LAURUS",
            currentPrice = 2018.00,
            priceChange = 63.50,
            changePercent = 3.25,
            volume = "2.1M",
            exchange = "NSE"
        )
    )

    val category = DashboardCategory(
        id = "gainers",
        name = "Top Gainers",
        viewAllText = "View All Top Gainers",
        apiType = "gainers",
        stocks = sampleStocks
    )

    MaterialTheme {
        CategoryCard(category = category)
    }
}

@Preview(showBackground = true, name = "Category Card - Losers")
@Composable
fun CategoryCardLosersPreview() {
    val loserStocks = listOf(
        StockData(
            id = "4",
            companyName = "Piramal Pharma",
            symbol = "PIRAMAL",
            currentPrice = 214.66,
            priceChange = -6.50,
            changePercent = -3.13,
            volume = "4.3M",
            exchange = "NSE"
        ),
        StockData(
            id = "5",
            companyName = "Torrent Pharma",
            symbol = "TORRENT",
            currentPrice = 4970.00,
            priceChange = -130.00,
            changePercent = -2.69,
            volume = "361K",
            exchange = "NSE"
        )
    )

    val category = DashboardCategory(
        id = "losers",
        name = "Top Losers",
        viewAllText = "View All Top Losers",
        apiType = "losers",
        stocks = loserStocks
    )

    MaterialTheme {
        CategoryCard(category = category)
    }
}

// ============================================
// STOCKS OVERVIEW CARD PREVIEW
// ============================================

private val overviewSampleStocks = listOf(
    StockData("1", "Mankind Pharma", "MANKIND", 2438.00, 137.00, 5.96, "1.3M", "NSE"),
    StockData("2", "Wockhardt", "WOCKHARDT", 2187.00, 101.00, 4.86, "1.7M", "NSE"),
    StockData("3", "Laurus Labs", "LAURUS", 2018.00, 63.50, 3.25, "2.1M", "NSE"),
    StockData("4", "Piramal Pharma", "PIRAMAL", 214.66, 6.50, 3.13, "4.3M", "NSE"),
    StockData("5", "Torrent Pharma", "TORRENT", 4970.00, -130.00, -2.69, "361K", "NSE"),
    StockData("6", "Biocon", "BIOCON", 395.00, -10.00, -2.60, "3M", "NSE")
)

private val overviewSampleCategories = listOf(
    "Top Gainers", "Top Losers", "Active by Volume", "Active by Value",
    "52 Week High", "52 Week Low", "My Watchlist"
).mapIndexed { index, name ->
    DashboardCategory(
        id = "tab$index",
        name = name,
        viewAllText = "View All $name",
        apiType = "tab$index",
        stocks = overviewSampleStocks
    )
}

@Preview(showBackground = true, name = "Stocks Overview Card", heightDp = 800)
@Composable
fun StocksOverviewCardPreview() {
    MaterialTheme {
        Box(modifier = Modifier.background(Color(0xFFFDF1EE)).padding(12.dp)) {
            StocksOverviewCard(
                filterName = "Nifty Pharma",
                categories = overviewSampleCategories,
                selectedCategoryId = "tab0",
                isLoading = false,
                errorMessage = "",
                onFilterClick = {},
                onCategorySelect = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Stocks Overview Card - Loading")
@Composable
fun StocksOverviewCardLoadingPreview() {
    MaterialTheme {
        Box(modifier = Modifier.background(Color(0xFFFDF1EE)).padding(12.dp)) {
            StocksOverviewCard(
                filterName = "Nifty 500",
                categories = emptyList(),
                selectedCategoryId = null,
                isLoading = true,
                errorMessage = "",
                onFilterClick = {},
                onCategorySelect = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Stocks Overview Card - Error")
@Composable
fun StocksOverviewCardErrorPreview() {
    MaterialTheme {
        Box(modifier = Modifier.background(Color(0xFFFDF1EE)).padding(12.dp)) {
            StocksOverviewCard(
                filterName = "Nifty 500",
                categories = emptyList(),
                selectedCategoryId = null,
                isLoading = false,
                errorMessage = "Failed to load configuration",
                onFilterClick = {},
                onCategorySelect = {}
            )
        }
    }
}

// ============================================
// BOTTOM NAVIGATION PREVIEW
// ============================================

@Preview(showBackground = true, name = "Bottom Navigation - Home Selected")
@Composable
fun BottomNavigationHomePreview() {
    MaterialTheme {
        BottomNavigationBar(
            currentScreen = Screen.HOME,
            onScreenSelected = {}
        )
    }
}

@Preview(showBackground = true, name = "Bottom Navigation - Watchlist Selected")
@Composable
fun BottomNavigationWatchlistPreview() {
    MaterialTheme {
        BottomNavigationBar(
            currentScreen = Screen.WATCHLIST,
            onScreenSelected = {}
        )
    }
}

@Preview(showBackground = true, name = "Bottom Navigation - Portfolio Selected")
@Composable
fun BottomNavigationPortfolioPreview() {
    MaterialTheme {
        BottomNavigationBar(
            currentScreen = Screen.PORTFOLIO,
            onScreenSelected = {}
        )
    }
}

@Preview(showBackground = true, name = "Bottom Navigation - Profile Selected")
@Composable
fun BottomNavigationProfilePreview() {
    MaterialTheme {
        BottomNavigationBar(
            currentScreen = Screen.PROFILE,
            onScreenSelected = {}
        )
    }
}