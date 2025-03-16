package com.example.socialmedia.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmedia.data.model.FollowsUserModel
import com.example.socialmedia.data.model.State
import com.example.socialmedia.domain.usecases.UserUsecase
import com.example.socialmedia.utils.ConnectionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowsViewModel @Inject constructor(
    private val userUseCase: UserUsecase
) : ViewModel() {
    
    private val _followsState =
        MutableStateFlow<State<List<FollowsUserModel>>>(State.Idle)
    val followsState = _followsState.asStateFlow()
    
    fun invokeFollows(userId: String, type: ConnectionType) =
        viewModelScope.launch {
            val result = if (type == ConnectionType.FOLLOWERS) {
                userUseCase.fetchUserFollowers(userId)
            } else {
                userUseCase.fetchUserFollowing(userId)
            }
            
            result.collectLatest { state ->
                _followsState.value = state
            }
        }
}