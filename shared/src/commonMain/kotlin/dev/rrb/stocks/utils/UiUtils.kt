package dev.rrb.stocks.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs

@Composable
fun PriceChangeDisplay(
    priceChange: Double,
    changePercent: Double,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 11.sp
) {
    val isPositive = priceChange >= 0
    val color = if (isPositive) Color(0xFF2E9E4F) else Color(0xFFD93025)
    val background = if (isPositive) Color(0xFFEAF6EC) else Color(0xFFFDECEA)
    val arrow = if (isPositive) "▲" else "▼"
    val displayValue = "$arrow ${abs(priceChange).toFormattedPrice()} (${abs(changePercent).toFormattedPercent()}%)"

    Row(
        modifier = modifier
            .background(background)
            .padding(horizontal = 6.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = displayValue,
            color = color,
            fontSize = fontSize
        )
    }
}


/*
@Composable
fun PriceChangeDisplay(
    value: Double,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 12.sp
) {
    val isPositive = value >= 0
    val color = if (isPositive) Color(0xFF4CAF50) else Color(0xFFF44336)
    val emoji = if (isPositive) "📈" else "📉"
    val sign = if (isPositive) "+" else ""
    val displayValue = "$sign${value.toFormattedPercent()}%"

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = emoji,
            fontSize = fontSize
        )
        Text(
            text = displayValue,
            color = color,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}*/
