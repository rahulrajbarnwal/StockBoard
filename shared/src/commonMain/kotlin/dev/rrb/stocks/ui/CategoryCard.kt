package dev.rrb.stocks.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.rrb.stocks.models.DashboardCategory
import dev.rrb.stocks.models.StockData
import dev.rrb.stocks.utils.PriceChangeDisplay
import dev.rrb.stocks.utils.toCompactVolume
import dev.rrb.stocks.utils.toFormattedPrice

@Composable
fun CategoryCard(category: DashboardCategory) {
    Column(
        modifier = Modifier
            .width(320.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(2.dp)
            )
            .shadow(1.dp, RoundedCornerShape(2.dp))
            .padding(16.dp)
    ) {
        Text(
            category.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F41BB),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Divider(
            color = Color(0xFFE0E0E0),
            thickness = 1.dp,
            modifier = Modifier.padding(bottom = 12.dp)
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
                    "Vol: ${stock.volume.toCompactVolume()}",
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
                PriceChangeDisplay(
                    priceChange = stock.priceChange,
                    changePercent = stock.changePercent,
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