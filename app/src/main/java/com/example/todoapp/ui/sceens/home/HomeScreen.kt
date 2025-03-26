package com.example.todoapp.ui.sceens.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.todoapp.MainViewModel
import com.example.todoapp.common.enum.LoadStatus
import com.fatherofapps.jnav.annotations.JNav

@Composable
@JNav(
    destination = "home_destination",
    baseRoute = "home_route",
    name = "HomeNavigation"
)
fun HomeScreen(
    viewModel: HomeViewModel,
    mainViewModel: MainViewModel
) {
    val tasks by viewModel.tasks.observeAsState(emptyList())
    var task by remember {
        mutableStateOf("")
    }
    val focusManager = LocalFocusManager.current
    val status by viewModel.uiState.collectAsState()
    Scaffold { paddingValue ->
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
                .background(
                    color = if (isSystemInDarkTheme()) Color(0xFF2F3C7E) else Color.White
                ),

            ) {
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "To-Do List", style = MaterialTheme.typography.headlineLarge.copy(
                    color = if (!isSystemInDarkTheme()) Color(0xFF0b2964) else Color(0xFFe1e2ec), fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(21.dp))
            OutlinedTextField(
                value = task,
                onValueChange = { task = it },
                shape = RoundedCornerShape(60.dp),
                colors = TextFieldDefaults.colors(unfocusedContainerColor = Color(0xFFe1e2ec), focusedContainerColor = Color(0xFFe1e2ec)),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(60.dp),
                trailingIcon = {
                    Button(
                        onClick = {
                            if (task.isNotEmpty()) {
                                viewModel.addTask(task)
                                focusManager.clearFocus()
                                task = ""
                            } else
                                mainViewModel.setError("Hãy nhập Task cần làm")
                        },
                        modifier = Modifier.size(width = 90.dp, height = 60.dp),
                        colors = ButtonColors(
                            containerColor = Color(0xFFff653e),
                            contentColor = Color.White,
                            disabledContentColor = Color(0xFFff653e),
                            disabledContainerColor = Color(0xFFff653e)
                        )
                    ) {
                        Text(text = "Add")
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        viewModel.addTask(task)
                        focusManager.clearFocus()
                        task = ""
                    }
                )
            )
            Log.e("status",status.status.toString())
            Spacer(modifier = Modifier.height(24.dp))
            if (status.status is LoadStatus.Loading){
                CircularProgressIndicator()
            }else{
                Lazy(viewModel = viewModel, tasks = tasks)
            }
        }
    }
}