package dev.rrb.stocks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.rrb.stocks.navigation.Screen
import dev.rrb.stocks.ui.BottomNavigationBar
import dev.rrb.stocks.ui.FilterScreen
import dev.rrb.stocks.ui.HomeScreen
import dev.rrb.stocks.viewmodel.HomeViewModel

@Composable
fun App() {
    MaterialTheme {
        val currentScreen = remember { mutableStateOf(Screen.HOME) }
        val showFilterScreen = remember { mutableStateOf(false) }
        val homeViewModel = viewModel<HomeViewModel>()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFAFAFA))
        ) {
            when (currentScreen.value) {
                Screen.HOME -> {
                    // ✅ Toggle between HomeScreen and FilterScreen
                    if (showFilterScreen.value) {
                        FilterScreen(
                            viewModel = homeViewModel,
                            onBackClick = { showFilterScreen.value = false }
                        )
                    } else {
                        HomeScreen(
                            modifier = Modifier.weight(1f),
                            viewModel = homeViewModel,
                            onFilterClick = { showFilterScreen.value = true }
                        )
                    }
                }

                Screen.WATCHLIST -> {
                    Text("Watchlist Screen")
                }

                Screen.PORTFOLIO -> {
                    Text("Portfolio Screen")
                }

                Screen.PROFILE -> {
                    Text("Profile Screen")
                }
            }

            // BottomNavigation only shows when NOT in FilterScreen
            if (!showFilterScreen.value) {
                BottomNavigationBar(
                    currentScreen = currentScreen.value,
                    onScreenSelected = { currentScreen.value = it }
                )
            }
        }
    }
}

@Composable
fun WatchlistScreen(modifier: Modifier = Modifier) {
    EmptyScreen(modifier)
}

@Composable
fun PortfolioScreen(modifier: Modifier = Modifier) {
    EmptyScreen(modifier)
}

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    EmptyScreen(modifier)
}

@Composable
fun EmptyScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        TopAppBar(
            title = {
                Text(
                    "Stock Market",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF1F41BB)
            )
        )
    }
}