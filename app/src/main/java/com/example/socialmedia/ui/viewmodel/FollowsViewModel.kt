package com.example.socialmedia.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmedia.data.db.local.AppDataStore
import com.example.socialmedia.data.model.FollowsUserModel
import com.example.socialmedia.data.model.State
import com.example.socialmedia.domain.usecases.UserUsecase
import com.example.socialmedia.utils.ConnectionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowsViewModel @Inject constructor(
    private val userUseCase: UserUsecase,
    private val appDataStore: AppDataStore,
) : ViewModel() {
    
    private val _followsState =
        MutableStateFlow<State<List<FollowsUserModel>>>(State.Idle)
    val followsState = _followsState.asStateFlow()
    
    private val _queryState = MutableStateFlow<String?>(null)
    val queryState = _queryState.asStateFlow()
    
    val username: StateFlow<String?> = appDataStore.username.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        ""
    )
    
    fun onChangeQuery(query: String) {
        _queryState.value = query
    }
    
    @OptIn(FlowPreview::class)
    fun observeQueryChange(type: ConnectionType) {
        viewModelScope.launch {
            _queryState.debounce(500).distinctUntilChanged()
                .collectLatest { query ->
                    if (!query.isNullOrBlank()) {
                        _followsState.value = State.Loading
                        invokeFollows(
                            query,
                            type
                        )
                    }
                }
        }
    }
    
    fun invokeFollows(userId: String, type: ConnectionType) =
        viewModelScope.launch {
            val result = if (type == ConnectionType.FOLLOWERS) {
                userUseCase.fetchUserFollowers(userId, _queryState.value)
            } else {
                userUseCase.fetchUserFollowing(userId, _queryState.value)
            }
            
            result.collectLatest { state ->
                _followsState.value = state
            }
        }
}