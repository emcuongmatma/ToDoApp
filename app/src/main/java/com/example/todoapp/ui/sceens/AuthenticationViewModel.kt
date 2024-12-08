package com.example.todoapp.ui.sceens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.common.enum.LoadStatus
import com.example.todoapp.data.repositories.Api
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthenticationUiState(
    var authStatus : Boolean = false,
    val status: LoadStatus = LoadStatus.Intit()
)


@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val api: Api?
):ViewModel() {
    private val _uiState = MutableStateFlow(AuthenticationUiState())
    val uiState = _uiState.asStateFlow()
    init {
        viewModelScope.launch {
            try {
                if (api?.isAuthenticated()!!) {
                    _uiState.value = _uiState.value.copy(authStatus = true)
                }
            } catch (ex: Exception) {
                _uiState.value = _uiState.value.copy(status = LoadStatus.Error(ex.toString()))
            }
        }
    }


}