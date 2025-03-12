package com.example.socialmedia.ui.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.socialmedia.data.model.State
import com.example.socialmedia.ui.components.AppOutlinedTextField
import com.example.socialmedia.ui.components.InitialSearchCompose
import com.example.socialmedia.ui.components.LoadingStaggeredGridCompose
import com.example.socialmedia.ui.viewmodel.PostViewModel
import com.example.socialmedia.ui.viewmodel.SearchViewModel
import com.example.socialmedia.utils.VerticalSpacer

@Composable
fun SearchScreen(
    navHostController: NavHostController,
    searchViewModel: SearchViewModel = hiltViewModel(),
    postViewModel: PostViewModel = hiltViewModel()
) {

    val searchState by searchViewModel.searchState.collectAsState()
    val query by searchViewModel.queryState.collectAsState()

    val postState by postViewModel.postState.collectAsState()

    LaunchedEffect(Unit) {
        postViewModel.fetchAllPosts()
    }

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
            /// CONTENT
            when (searchState) {
                is State.Loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }

                is State.Success -> Text("Results: ${(searchState as State.Success).data}")
                is State.Failure -> Text("Error: ${(searchState as State.Failure).throwable.message}")
                State.Idle -> when (postState) {
                    is State.Success -> {
                        val items = (postState as State.Success).data
                        if (items.isEmpty()) {
                            LoadingStaggeredGridCompose()
                            return@Scaffold
                        }
                        InitialSearchCompose(postViewModel, items)
                    }

                    is State.Failure -> {}
                    is State.Loading -> {
                        LoadingStaggeredGridCompose()
                    }

                    else -> {}
                }
            }
        }
    }
}