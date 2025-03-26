package com.example.todoapp.ui.sceens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.Result
import com.example.todoapp.data.repositories.Api
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthenticationUiState(
    var authStatus : Boolean = false
)


@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val api: Api?
):ViewModel() {
    private val _uiState = MutableStateFlow(AuthenticationUiState())
    val uiState = _uiState.asStateFlow()
    init {
        viewModelScope.launch {
            val result = api?.getCurrentUser()
            if (result is Result.Success) _uiState.value.authStatus = true
        }
    }


}