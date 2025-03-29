package com.example.todoapp.ui.sceens.login


import android.text.TextUtils
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.common.enum.LoadStatus
import com.example.todoapp.data.Result
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
    val status: LoadStatus = LoadStatus.Init()
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
            val username = if (!uiState.value.username.isEmailValid()) uiState.value.username + "@todoapp.com" else uiState.value.username
            when (val result = api?.login(
                    username = username,
                    password = _uiState.value.password
                )) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(status = LoadStatus.Success())
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(status = LoadStatus.Error(result.exception))
                }
                is Result.Loading -> {
                    _uiState.value = _uiState.value.copy(status = LoadStatus.Loading())
                }
                else -> {}
            }
        }
    }

    fun signup() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(status = LoadStatus.Loading())
            val username = if (!uiState.value.username.isEmailValid()) uiState.value.username + "@todoapp.com" else uiState.value.username
            when(val result = api?.signup(
                username = username,
                password = _uiState.value.password,
                cpwd = _uiState.value.confirmpwd
            )){
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(status = LoadStatus.Success())
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(status = LoadStatus.Error(result.exception))
                }
                is Result.Loading -> {
                    _uiState.value = _uiState.value.copy(status = LoadStatus.Loading())
                }
                else ->{}
            }
        }
    }

    fun reset() {
        _uiState.value = _uiState.value.copy(status = LoadStatus.Init())
    }
}

fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}