package com.example.todoapp.ui.sceens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.common.enum.LoadStatus
import com.example.todoapp.data.repositories.Api
import com.example.todoapp.data.repositories.MainLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val confirmpwd: String = "",
    val status: LoadStatus = LoadStatus.Intit()
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    @Suppress("unused") private val log: MainLog?,
    private val api: Api?
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()
    fun updateUsername(username: String) {
        _uiState.value = _uiState.value.copy(username = username)
    }

    fun updatePwd(pwd: String) {
        _uiState.value = _uiState.value.copy(password = pwd)
    }

    fun updatecpwd(cpwd: String) {
        _uiState.value = _uiState.value.copy(confirmpwd = cpwd)
    }

    fun login() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(status = LoadStatus.Loading())
            try {
                @Suppress("UNUSED_VARIABLE") var result = api?.login(uiState.value.username, uiState.value.password)
                _uiState.value = _uiState.value.copy(status = LoadStatus.Success())
            } catch (ex: Exception) {
                updatePwd("")
                _uiState.value =
                    _uiState.value.copy(status = LoadStatus.Error(ex.message.toString()))
            }
        }

    }

    fun signup() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(status = LoadStatus.Loading())
            try {
                @Suppress("UNUSED_VARIABLE") var result = api?.signup(uiState.value.username, uiState.value.password,uiState.value.confirmpwd)
                _uiState.value = _uiState.value.copy(status = LoadStatus.Success())
            } catch (ex: Exception) {
                updatecpwd("")
                updatePwd("")
                _uiState.value =
                    _uiState.value.copy(status = LoadStatus.Error(ex.message.toString()))
            }

        }
    }

    fun reset() {
        _uiState.value = _uiState.value.copy(status = LoadStatus.Intit())
    }
}