package com.example.todoapp.ui.sceens.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.common.enum.LoadStatus
import com.example.todoapp.data.repositories.Api
import com.example.todoapp.data.repositories.MainLog
import com.example.todoapp.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val tasks: List<Task> = emptyList(),
    val status: LoadStatus = LoadStatus.Intit(),
    val selectedIndex: Int = -1,
    val isOpenDialog: Boolean = false,
    val tmpTask : Task = Task()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    @Suppress("unused") private val log: MainLog?,
    private val api: Api?
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadTasks()
    }

    fun loadTasks() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(status = LoadStatus.Loading())
            try {
                if (api != null) {
                    val loadTasks = api.loadTasks()
                    _uiState.value =
                        _uiState.value.copy(tasks = loadTasks, status = LoadStatus.Success())
                }
            } catch (ex: Exception) {
                _uiState.value =
                    _uiState.value.copy(status = LoadStatus.Error(ex.message.toString()))
            }
        }
    }

    fun deleteTask(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(status = LoadStatus.Loading())
            try {
                if (api != null) {
                    api.delTask(id)
                    val loadTasks = api.loadTasks()
                    _uiState.value =
                        _uiState.value.copy(tasks = loadTasks, status = LoadStatus.Success())
                }
            } catch (ex: Exception) {
                _uiState.value =
                    _uiState.value.copy(status = LoadStatus.Error(ex.message.toString()))
            }
        }
    }

    fun addTask(task: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(status = LoadStatus.Loading())
            try {
                if (api != null) {
                    api.addTask(task)
                    val loadTasks = api.loadTasks()
                    _uiState.value =
                        _uiState.value.copy(tasks = loadTasks, status = LoadStatus.Success())
                }
            } catch (ex: Exception) {
                _uiState.value =
                    _uiState.value.copy(status = LoadStatus.Error(ex.message.toString()))
            }
        }

    }

    fun editTask(task: Task) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(status = LoadStatus.Loading())
            try {
                if (api != null) {
                    api.updateTask(task.taskId, task.title, task.createdAt,task.isCompleted)
                    val loadTasks = api.loadTasks()
                    _uiState.value =
                        _uiState.value.copy(tasks = loadTasks, status = LoadStatus.Success())
                }
            } catch (ex: Exception) {
                _uiState.value =
                    _uiState.value.copy(status = LoadStatus.Error(ex.message.toString()))
            }
        }
    }

    fun reset() {
        _uiState.value = _uiState.value.copy(status = LoadStatus.Intit())
    }
    fun updateOpenDialog(isOpenDialog: Boolean){
        _uiState.value = _uiState.value.copy(isOpenDialog = isOpenDialog)
    }
    fun setTmpTask(task: Task){
        _uiState.value=_uiState.value.copy(tmpTask = task)
    }

}