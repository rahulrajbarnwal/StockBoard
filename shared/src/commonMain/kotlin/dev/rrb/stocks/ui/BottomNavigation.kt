package dev.rrb.stocks.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.rrb.stocks.navigation.Screen

data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val icon: String  // ✅ Unicode emoji instead
)

@Composable
fun BottomNavigationBar(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit
) {
    val items = listOf(
        BottomNavItem(Screen.HOME, "Home", "🏠"),
        BottomNavItem(Screen.WATCHLIST, "Watchlist", "⭐"),
        BottomNavItem(Screen.PORTFOLIO, "Portfolio", "💼"),
        BottomNavItem(Screen.PROFILE, "Profile", "👤")
    )

    NavigationBar(
        modifier = Modifier
            .background(Color.White),
        containerColor = Color.White,
        contentColor = Color(0xFF1F41BB)
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Text(
                        text = item.icon,
                        fontSize = 24.sp
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 11.sp
                    )
                },
                selected = currentScreen == item.screen,
                onClick = { onScreenSelected(item.screen) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF1F41BB),
                    selectedTextColor = Color(0xFF1F41BB),
                    unselectedIconColor = Color(0xFF999999),
                    unselectedTextColor = Color(0xFF999999),
                    indicatorColor = Color(0xFFE3F2FD)
                )
            )
        }
    }
}