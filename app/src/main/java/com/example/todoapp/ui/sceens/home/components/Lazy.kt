package com.example.todoapp.ui.sceens.home.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todoapp.model.Task
import com.example.todoapp.ui.sceens.home.HomeViewModel

@Composable
fun Lazy(
    viewModel: HomeViewModel,
    tasks : List<Task>
) {
    val state by viewModel.uiState.collectAsState()
    LazyColumn {
        items(items = tasks, key = { it.taskId }) { item ->
            Spacer(modifier = Modifier.height(10.dp))
            TaskItem(
                task = item,
                onEditClick = {
                    viewModel.updateOpenDialog(true)
                    viewModel.setTmpTask(item)
                },
                onDeleteClick = { taskId ->
                    viewModel.deleteTask(taskId)
                },
                onToggleDone = { isDone ->
                    viewModel.editTask(item.copy(isCompleted = isDone))
                }
            )
        }
    }
    if (state.isOpenDialog) DialogEditor(viewModel)
}
