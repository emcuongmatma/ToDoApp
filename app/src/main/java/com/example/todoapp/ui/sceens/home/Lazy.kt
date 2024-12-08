package com.example.todoapp.ui.sceens.home

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todoapp.model.Task

@Composable
fun Lazy(
    viewModel: HomeViewModel
) {
    val state = viewModel.uiState.collectAsState()
    LazyColumn {
        items(items = state.value.tasks, key = { it.taskId }) { item ->
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
                    viewModel.editTask(Task(item.taskId, item.title, item.createdAt, isDone))
                }
            )
        }
    }
    Log.e("Recompose","sdf")
    if (state.value.isOpenDialog) DialogEditor(viewModel)
}
