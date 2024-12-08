package com.example.todoapp.ui.sceens.home

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

@Composable
fun DialogEditor(viewModel: HomeViewModel) {
    val state = viewModel.uiState.collectAsState()
    var newTile by remember {
        mutableStateOf(viewModel.uiState.value.tmpTask.title)
    }
    AlertDialog(
        onDismissRequest = { viewModel.updateOpenDialog(false) },
        title = { Text(text = "Nhập tên mới cho task") },
        containerColor = Color(0xFFe1e2ec),
        titleContentColor = Color(0xFF0b2964),
        text = { OutlinedTextField(value = newTile, onValueChange = { newTile = it }) },
        confirmButton = {
            Button(
                onClick = {
                    val tmpTask = state.value.tmpTask.copy(title = newTile)
                    viewModel.editTask(tmpTask)
                    viewModel.updateOpenDialog(false)
                },
                colors = ButtonColors(
                    containerColor = Color(0xFFff653e),
                    contentColor = Color.White,
                    disabledContentColor = Color(0xFFff653e),
                    disabledContainerColor = Color(0xFFff653e)
                )
            ) {
                Text(text = "Update Data")
            }
        }
    )
}