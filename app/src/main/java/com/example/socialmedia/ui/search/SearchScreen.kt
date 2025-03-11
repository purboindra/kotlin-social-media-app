package com.example.socialmedia.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.socialmedia.data.model.State
import com.example.socialmedia.ui.components.AppOutlinedTextField
import com.example.socialmedia.ui.viewmodel.SearchViewModel
import com.example.socialmedia.utils.VerticalSpacer

@Composable
fun SearchScreen(
    navHostController: NavHostController,
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    
    val searchState by searchViewModel.searchState.collectAsState()
    val query by searchViewModel.queryState.collectAsState()
    
    Scaffold { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            /// SEARCH
            AppOutlinedTextField(
                query = query,
                onValueChange = {
                    searchViewModel.onChangeQuery(it)
                },
                placeholderText = "Search...",
                trailingIcon = {
                    Icon(
                        Icons.Outlined.Search,
                        contentDescription = "Search",
                        modifier = Modifier.size(18.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 12.dp
                    )
            )
            5.VerticalSpacer()
            when (searchState) {
                is State.Loading -> Text("Loading...")
                is State.Success -> Text("Results: ${(searchState as State.Success).data}")
                is State.Failure -> Text("Error: ${(searchState as State.Failure).throwable.message}")
                State.Idle -> Text("Type something to search...")
            }
            /// CONTENT
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(3),
                verticalItemSpacing = 4.dp,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
            ) {
                items(10) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height((100..300).random().dp)
                            .background(Color.LightGray)
                    )
                }
                
            }
        }
    }
}