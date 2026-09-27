package dev.rrb.stocks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
        val homeViewModel = viewModel { HomeViewModel() }

        Scaffold(
            containerColor = Color(0xFFFAFAFA),
            // Each screen's TopAppBar handles the status bar itself; the NavigationBar handles the bottom
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = {
                // BottomNavigation only shows when NOT in FilterScreen
                if (!showFilterScreen.value) {
                    BottomNavigationBar(
                        currentScreen = currentScreen.value,
                        onScreenSelected = { currentScreen.value = it }
                    )
                }
            }
        ) { innerPadding ->
            val screenModifier = Modifier.padding(innerPadding)

            when (currentScreen.value) {
                Screen.HOME -> {
                    if (showFilterScreen.value) {
                        FilterScreen(
                            viewModel = homeViewModel,
                            onBackClick = { showFilterScreen.value = false }
                        )
                    } else {
                        HomeScreen(
                            modifier = screenModifier,
                            viewModel = homeViewModel,
                            onFilterClick = { showFilterScreen.value = true }
                        )
                    }
                }

                Screen.WATCHLIST -> PlaceholderScreen("Watchlist", screenModifier)
                Screen.PORTFOLIO -> PlaceholderScreen("Portfolio", screenModifier)
                Screen.PROFILE -> PlaceholderScreen("Profile", screenModifier)
            }
        }
    }
}

@Composable
fun PlaceholderScreen(title: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(title, fontWeight = FontWeight.Bold) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF1F41BB),
                titleContentColor = Color.White
            )
        )
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("$title coming soon", color = Color(0xFF999999))
        }
    }
}
