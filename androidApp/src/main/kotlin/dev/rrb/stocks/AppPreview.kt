package dev.rrb.stocks

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.rrb.stocks.models.StockData
import dev.rrb.stocks.models.DashboardCategory

@Preview(showBackground = true, name = "Positive Stock")
@Composable
fun StockItemPreview() {
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

@Preview(showBackground = true, name = "Negative Stock")
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

@Preview(showBackground = true, name = "Category Card Preview")
@Composable
fun CategoryCardPreview() {
    val sampleStocks = listOf(
        StockData(
            id = "1",
            companyName = "Stock 1",
            symbol = "STK1",
            currentPrice = 100.0,
            priceChange = 10.0,
            changePercent = 5.0,
            volume = "1M",
            exchange = "NSE"
        ),
        StockData(
            id = "2",
            companyName = "Stock 2",
            symbol = "STK2",
            currentPrice = 200.0,
            priceChange = -20.0,
            changePercent = -5.0,
            volume = "2M",
            exchange = "NSE"
        )
    )

    val category = DashboardCategory(
        id = "gainers",
        name = "Top Gainers",
        viewAllText = "View All",
        apiType = "gainers",
        stocks = sampleStocks
    )

    MaterialTheme {
        CategoryCard(category = category)
    }
}