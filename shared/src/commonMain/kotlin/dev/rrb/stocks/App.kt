package dev.rrb.stocks

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.tooling.preview.Preview
import dev.rrb.stocks.models.DashboardCategory
import dev.rrb.stocks.models.StockData
import dev.rrb.stocks.viewmodel.HomeViewModel
import dev.rrb.stocks.utils.toFormattedPercent
import dev.rrb.stocks.utils.toFormattedPrice
import dev.rrb.stocks.utils.PriceChangeDisplay


@Composable
fun App() {
    MaterialTheme {
        HomeScreen()
    }
}

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val categories = viewModel.categories.collectAsState().value
    val filterName = viewModel.filterName.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        // Top Bar
        TopAppBar(
            title = {
                Text(
                    "Stock Market",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF1F41BB)
            )
        )

        // Header with Filter
        HeaderSection(filterName = filterName)

        // Categories and Stocks
        CategoriesSection(categories = categories)
    }
}

@Composable
fun HeaderSection(filterName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Stocks Overview",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1a1a1a)
        )

        OutlinedButton(
            onClick = { },
            modifier = Modifier.height(40.dp),
            shape = RoundedCornerShape(20.dp),
            border = ButtonDefaults.outlinedButtonBorder
        ) {
            Text(
                filterName,
                fontSize = 12.sp,
                color = Color(0xFF1a1a1a)
            )
        }
    }
}

@Composable
fun CategoriesSection(categories: List<DashboardCategory>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            CategoryCard(category = category)
        }
    }
}

@Composable
fun CategoryCard(category: DashboardCategory) {
    Column(
        modifier = Modifier
            .width(300.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Text(
            category.name,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Divider(
            color = Color(0xFFE0E0E0),
            thickness = 1.dp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        category.stocks.forEach { stock ->
            StockItem(stock = stock)
        }
    }
}

@Composable
fun StockItem(stock: StockData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    stock.companyName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1a1a1a)
                )
                Text(
                    "Vol: ${stock.volume}",
                    fontSize = 12.sp,
                    color = Color(0xFF999999),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    stock.currentPrice.toFormattedPrice(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1a1a1a)
                )
                // ✅ REPLACED WITH PriceChangeDisplay
                PriceChangeDisplay(
                    value = stock.changePercent,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Divider(
            color = Color(0xFFE0E0E0),
            thickness = 0.5.dp,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
/*

// ✅ PREVIEW
@Preview(showBackground = true)
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

@Preview(showBackground = true)
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
}*/
