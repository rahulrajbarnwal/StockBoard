package dev.rrb.stocks.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.rrb.stocks.models.DashboardCategory
import dev.rrb.stocks.models.StockData
import dev.rrb.stocks.utils.PriceChangeDisplay
import dev.rrb.stocks.utils.toCompactVolume
import dev.rrb.stocks.utils.toFormattedPrice

private val RowHeight = 72.dp
private val TabWidth = 84.dp

private val TextPrimary = Color(0xFF1A1A1A)
private val TextSecondary = Color(0xFF9E9E9E)
private val DividerColor = Color(0xFFE6E6E6)
private val TabBackground = Color(0xFFFFF4F2)
private val TabBorder = Color(0xFFE2C9C3)
private val LossColor = Color(0xFFD93025)
private val AccentBlue = Color(0xFF2F80D1)

@Composable
fun StocksOverviewCard(
    filterName: String,
    categories: List<DashboardCategory>,
    selectedCategoryId: String?,
    isLoading: Boolean,
    errorMessage: String,
    onFilterClick: () -> Unit,
    onCategorySelect: (DashboardCategory) -> Unit,
    onViewAllClick: (DashboardCategory) -> Unit = {},
    onAddStockClick: (StockData) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val selectedCategory = categories.firstOrNull { it.id == selectedCategoryId }
        ?: categories.firstOrNull()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(8.dp))
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(bottom = 16.dp)
    ) {
        OverviewHeader(filterName = filterName, onFilterClick = onFilterClick)

        HorizontalDivider(
            color = DividerColor,
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        when {
            isLoading -> Box(
                modifier = Modifier.fillMaxWidth().height(RowHeight * 3),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            errorMessage.isNotEmpty() -> Text(
                errorMessage,
                color = LossColor,
                fontSize = 14.sp,
                modifier = Modifier.padding(16.dp)
            )

            selectedCategory != null -> {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.width(TabWidth)) {
                        categories.forEach { category ->
                            CategoryTab(
                                name = category.name,
                                isSelected = category.id == selectedCategory.id,
                                onClick = { onCategorySelect(category) }
                            )
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        if (selectedCategory.stocks.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxWidth().height(RowHeight),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No stocks", fontSize = 14.sp, color = TextSecondary)
                            }
                        } else {
                            selectedCategory.stocks.forEach { stock ->
                                OverviewStockRow(
                                    stock = stock,
                                    onAddClick = { onAddStockClick(stock) }
                                )
                            }
                        }
                    }
                }

                OutlinedButton(
                    onClick = { onViewAllClick(selectedCategory) },
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .padding(top = 20.dp)
                        .height(44.dp)
                ) {
                    Text(
                        selectedCategory.viewAllText.ifBlank { "View All ${selectedCategory.name}" },
                        fontSize = 15.sp,
                        color = TextPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun OverviewHeader(filterName: String, onFilterClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Stocks Overview",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Row(
            modifier = Modifier
                .border(1.dp, TextPrimary, RoundedCornerShape(50))
                .clickable(onClick = onFilterClick)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                filterName.uppercase(),
                fontSize = 12.sp,
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.width(6.dp))
            FunnelIcon(size = 14.dp, color = TextPrimary)
        }
    }
}

@Composable
private fun CategoryTab(name: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(RowHeight)
            .then(
                if (isSelected) Modifier.background(Color.White)
                else Modifier.background(TabBackground).border(0.5.dp, TabBorder)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            name,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = TextPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
        if (isSelected) {
            Text("›", fontSize = 18.sp, color = TextPrimary)
        }
    }
}

@Composable
private fun OverviewStockRow(stock: StockData, onAddClick: () -> Unit) {
    // Divider sits inside the fixed height so each row matches a CategoryTab exactly
    Column(modifier = Modifier.fillMaxWidth().height(RowHeight)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 12.dp, end = 8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            // Top line: name on the left, price pinned to the end
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stock.companyName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    stock.currentPrice.toFormattedPrice(),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // Bottom line: volume, change chip, and + at the bottom end
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Vol: ${stock.volume.toCompactVolume()}",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    modifier = Modifier.weight(1f)
                )
                PriceChangeDisplay(
                    priceChange = stock.priceChange,
                    changePercent = stock.changePercent
                )
                Text(
                    "+",
                    fontSize = 22.sp,
                    lineHeight = 22.sp,
                    color = AccentBlue,
                    modifier = Modifier
                        .clickable(onClick = onAddClick)
                        .padding(start = 10.dp, end = 2.dp)
                )
            }
        }
        HorizontalDivider(color = DividerColor, thickness = 1.dp)
    }
}

@Composable
private fun FunnelIcon(size: Dp, color: Color) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(w, 0f)
            lineTo(w * 0.6f, h * 0.5f)
            lineTo(w * 0.6f, h)
            lineTo(w * 0.4f, h * 0.85f)
            lineTo(w * 0.4f, h * 0.5f)
            close()
        }
        drawPath(path, color, style = Stroke(width = 1.2.dp.toPx(), join = StrokeJoin.Round))
    }
}
