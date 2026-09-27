package dev.rrb.stocks.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import dev.rrb.stocks.navigation.Screen
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import stocks.shared.generated.resources.Res
import stocks.shared.generated.resources.ic_home_filled
import stocks.shared.generated.resources.ic_home_outlined
import stocks.shared.generated.resources.ic_person_filled
import stocks.shared.generated.resources.ic_person_outlined
import stocks.shared.generated.resources.ic_star_filled
import stocks.shared.generated.resources.ic_star_outlined
import stocks.shared.generated.resources.ic_work_filled
import stocks.shared.generated.resources.ic_work_outlined

data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val selectedIcon: DrawableResource,
    val unselectedIcon: DrawableResource
)

private val bottomNavItems = listOf(
    BottomNavItem(Screen.HOME, "Home", Res.drawable.ic_home_filled, Res.drawable.ic_home_outlined),
    BottomNavItem(Screen.WATCHLIST, "Watchlist", Res.drawable.ic_star_filled, Res.drawable.ic_star_outlined),
    BottomNavItem(Screen.PORTFOLIO, "Portfolio", Res.drawable.ic_work_filled, Res.drawable.ic_work_outlined),
    BottomNavItem(Screen.PROFILE, "Profile", Res.drawable.ic_person_filled, Res.drawable.ic_person_outlined)
)

@Composable
fun BottomNavigationBar(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = Color(0xFF1F41BB)
    ) {
        bottomNavItems.forEach { item ->
            val selected = currentScreen == item.screen
            NavigationBarItem(
                icon = {
                    // Icon tints from LocalContentColor, so the colors below apply to it
                    Icon(
                        painter = painterResource(if (selected) item.selectedIcon else item.unselectedIcon),
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 11.sp
                    )
                },
                selected = selected,
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
