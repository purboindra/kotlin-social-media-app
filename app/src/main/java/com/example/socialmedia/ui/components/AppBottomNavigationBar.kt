package com.example.socialmedia.ui.components

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class BottomNavigationItem(
    val title: String,
    val selectedItem: ImageVector,
    val unSelectedItem: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null,
    val route: String,
)

@Composable
fun AppBottomNavigationBar(
    items: List<BottomNavigationItem>,
    selectedItem: String,
    onSelectedItem: (String) -> Unit
) {
    NavigationBar(
        contentColor = Color.White,
        containerColor = Color.White,
        tonalElevation = 0.dp,
    ) {
        items.forEachIndexed { index, i ->
            NavigationBarItem(
                selected = selectedItem == i.route,
                onClick = {
                    onSelectedItem(i.route)
                },
                icon = {
                    BadgedBox(
                        badge = {
                            if (i.badgeCount != null) {
                                Badge {
                                    Text(text = i.badgeCount.toString())
                                }
                            } else if (i.hasNews) {
                                Badge()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (i.route == selectedItem) i.selectedItem else i.unSelectedItem,
                            contentDescription = i.title
                        )
                    }
                },
                alwaysShowLabel = false,
                label = {
                    i.title
                }
            )
            
        }
    }
}