package com.example.socialmedia.ui.main

import android.provider.CalendarContract.Colors
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MovieCreation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MovieCreation
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.socialmedia.data.db.local.AppDataStore
import com.example.socialmedia.ui.components.AppBottomNavigationBar
import com.example.socialmedia.ui.components.BottomNavigationItem
import com.example.socialmedia.ui.viewmodel.MainViewModel

@Composable
fun MainScreen(
    navHostController: NavHostController,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    
    val emailFlow = remember { AppDataStore(context) }
    val email by emailFlow.email.collectAsState("")
    val bottomNavbarIndex by mainViewModel.bottomNavbarIndex.collectAsState()
    
    val items = listOf(
        BottomNavigationItem(
            title = "Home",
            selectedItem = Icons.Filled.Home,
            unSelectedItem = Icons.Outlined.Home,
            hasNews = false
        ),
        BottomNavigationItem(
            title = "Search",
            selectedItem = Icons.Filled.Search,
            unSelectedItem = Icons.Outlined.Search,
            hasNews = false
        ),
        BottomNavigationItem(
            title = "Reels",
            selectedItem = Icons.Filled.MovieCreation,
            unSelectedItem = Icons.Outlined.MovieCreation,
            hasNews = false
        ),
        BottomNavigationItem(
            title = "Profile",
            selectedItem = Icons.Filled.Person,
            unSelectedItem = Icons.Outlined.Person,
            hasNews = false
        ),
    )
    
    Scaffold(
        bottomBar = {
            AppBottomNavigationBar(
                items = items,
                selectedItem = bottomNavbarIndex,
                onSelectedItem = { mainViewModel.onSelectedBottomNavbar(it) }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .safeContentPadding()
                .statusBarsPadding(),
        ) {
            item {
                Text("Hello Main: $email")
            }
        }
    }
}