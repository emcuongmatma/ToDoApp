package com.example.todoapp.ui.sceens.home


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.common.enum.LoadStatus
import com.example.todoapp.data.Result
import com.example.todoapp.data.repositories.Api
import com.example.todoapp.data.repositories.MainLog
import com.example.todoapp.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isOpenDialog: Boolean = false,
    val tmpTask: Task = Task(),
    val status: LoadStatus = LoadStatus.Loading()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    @Suppress("unused") private val log: MainLog?,
    private val api: Api?
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()
    private var _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> get() = _tasks
    private val _currentUser = MutableLiveData<String>()

    //    val currentUser: MutableLiveData<String> get() = _currentUser
    init {
        loadCurrentUser()
        loadTasks()
        _uiState.value = _uiState.value.copy(status = LoadStatus.Init())
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            when (val result = api!!.getCurrentUser()) {
                is Result.Success -> {
                    _currentUser.value = result.data
                }
                is Result.Error -> {}
                is Result.Loading -> {}
            }
        }
    }

    fun loadTasks() {
        viewModelScope.launch {
            if (_currentUser.value != null) {
                api!!.loadTasks(_currentUser.value!!).collect {
                    _tasks.value = it
                }
            }
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (api!!.delTask(taskId, _currentUser.value!!)) {
                is Result.Success -> {}
                is Result.Error -> {}
                is Result.Loading -> {
                }
            }
        }
    }

    fun addTask(task: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (api!!.addTask(title = task, username = _currentUser.value!!)) {
                is Result.Success -> {}
                is Result.Error -> {}
                is Result.Loading -> {
                }
            }
        }
    }

    fun editTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            when (api!!.updateTask(task, _currentUser.value!!)) {
                is Result.Success -> {}
                is Result.Error -> {}
                is Result.Loading -> {
                }
            }
        }
    }

    fun updateOpenDialog(isOpenDialog: Boolean) {
        _uiState.value = _uiState.value.copy(isOpenDialog = isOpenDialog)
    }

    fun setTmpTask(task: Task) {
        _uiState.value = _uiState.value.copy(tmpTask = task)
    }

}