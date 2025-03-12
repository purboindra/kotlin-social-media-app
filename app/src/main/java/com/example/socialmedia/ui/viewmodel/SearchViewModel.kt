package com.example.socialmedia.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmedia.data.model.State
import com.example.socialmedia.data.model.UserModel
import com.example.socialmedia.domain.usecases.UserUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val userUsecase: UserUsecase,
) : ViewModel() {

    private val _searchState =
        MutableStateFlow<State<List<UserModel>>>(State.Idle)
    val searchState = _searchState.asStateFlow()

    private val _queryState = MutableStateFlow("")
    val queryState = _queryState.asStateFlow()

    init {
        observeQueryChange()
    }

    @OptIn(FlowPreview::class)
    private fun observeQueryChange() {
        viewModelScope.launch {
            _queryState.debounce(
                500
            ).distinctUntilChanged().collectLatest { query ->
                if (query.isNotBlank()) {
                    _searchState.value = State.Loading
                    userUsecase.search(query)
                        .collectLatest { state ->
                            _searchState.value = state
                        }
                } else {
                    _searchState.value = State.Idle
                }
            }
        }
    }

    fun onChangeQuery(query: String) {
        if (query.isBlank()) {
            _searchState.value = State.Idle
        }
        _queryState.value = query
    }
}