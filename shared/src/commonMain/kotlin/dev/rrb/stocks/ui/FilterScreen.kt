package dev.rrb.stocks.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.rrb.stocks.models.FilterOption
import dev.rrb.stocks.viewmodel.HomeViewModel

@Composable
fun FilterScreen(
    viewModel: HomeViewModel,
    onBackClick: () -> Unit
) {
    val filterOptions = viewModel.filterOptions.collectAsState().value
    val currentFilterName = viewModel.filterName.collectAsState().value

    var selectedExchange by remember { mutableStateOf("NSE") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(filterOptions) {
        if (selectedCategory == null && filterOptions.isNotEmpty()) {
            selectedCategory = filterOptions.keys.first()
        }
    }

    val categories = filterOptions.keys.toList()

    val categoryItems = if (selectedCategory != null) {
        filterOptions[selectedCategory]
            ?.filter { it.exchange == selectedExchange }
            ?: emptyList()
    } else {
        emptyList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // ✅ Toolbar below status bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFAFAFA))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    "←",
                    fontSize = 24.sp,
                    modifier = Modifier.clickable { onBackClick() }
                )
                Text(
                    "Filter",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Row(
                modifier = Modifier
                    .background(
                        color = Color(0xFFE0E0E0),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp)
                    )
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ExchangeButton(
                    text = "NSE",
                    isSelected = selectedExchange == "NSE",
                    onClick = { selectedExchange = "NSE" }
                )
                ExchangeButton(
                    text = "BSE",
                    isSelected = selectedExchange == "BSE",
                    onClick = { selectedExchange = "BSE" }
                )
            }
        }

        Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)

        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight()
                    .background(Color(0xFFF5F5F5))
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    items(categories) { category ->
                        CategoryRow(
                            categoryName = category,
                            isSelected = selectedCategory == category,
                            onSelect = {
                                selectedCategory = category
                            }
                        )
                    }
                }
            }

            Divider(
                color = Color(0xFFE0E0E0),
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            )

            Column(
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxHeight()
            ) {
                if (categoryItems.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "No items",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        items(categoryItems) { filterOption ->
                            FilterItemRow(
                                filterOption = filterOption,
                                isSelected = currentFilterName == filterOption.name,
                                onSelect = {
                                    // ✅ Pass filterType (always "index" from API)
                                    viewModel.updateFilter(filterOption, "index")
                                    onBackClick()  // ✅ Close filter screen
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExchangeButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .height(32.dp)
            .width(60.dp),
        colors = ButtonColors(
            containerColor = if (isSelected) Color.Black else Color.Transparent,
            contentColor = if (isSelected) Color.White else Color.Black,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.Gray
        ),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
    ) {
        Text(
            text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CategoryRow(
    categoryName: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (isSelected) Color(0xFFE3F2FD) else Color.Transparent
            )
            .clickable { onSelect() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            categoryName,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color(0xFF1F41BB) else Color(0xFF1a1a1a),
            modifier = Modifier.weight(1f)
        )

        if (isSelected) {
            Text(
                "→",
                fontSize = 16.sp,
                color = Color(0xFF1F41BB)
            )
        }
    }

    Divider(color = Color(0xFFDDDDDD), thickness = 0.5.dp)
}

@Composable
fun FilterItemRow(
    filterOption: FilterOption,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (isSelected) Color(0xFFE3F2FD) else Color.White
            )
            .clickable { onSelect() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            filterOption.name ?: "Unknown",
            fontSize = 13.sp,
            color = if (isSelected) Color(0xFF1F41BB) else Color(0xFF1a1a1a),
            modifier = Modifier.weight(1f)
        )

        if (isSelected) {
            Text(
                "✓",
                fontSize = 16.sp,
                color = Color(0xFF1F41BB),
                fontWeight = FontWeight.Bold
            )
        }
    }

    Divider(color = Color(0xFFEEEEEE), thickness = 0.5.dp)
}