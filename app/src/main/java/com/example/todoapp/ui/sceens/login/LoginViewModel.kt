package com.example.todoapp.ui.sceens.login


import androidx.lifecycle.MutableLiveData
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
    private val _authResult = MutableLiveData<Result<Boolean>?>()
    val authResult: MutableLiveData<Result<Boolean>?> get() = _authResult
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
            _authResult.value =
                api?.login(username = _uiState.value.username, password = _uiState.value.password)
            _uiState.value = _uiState.value.copy(status = LoadStatus.Init())
        }

    }

    fun signup() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(status = LoadStatus.Loading())
            _authResult.value = api?.signup(
                username = _uiState.value.username,
                password = _uiState.value.password,
                cpwd = _uiState.value.confirmpwd
            )
            _uiState.value = _uiState.value.copy(status = LoadStatus.Init())
        }
    }

    fun reset() {
        _authResult.value = null
        _uiState.value = _uiState.value.copy(status = LoadStatus.Init())
    }
}