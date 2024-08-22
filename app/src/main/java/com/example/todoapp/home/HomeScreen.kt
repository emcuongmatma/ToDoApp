package com.example.todoapp.home

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.todoapp.R
import com.example.todoapp.data.model.Task
import com.example.todoapp.state.LazyScreen
import com.example.todoapp.state.LoadingDialog
import com.example.todoapp.state.State
import com.example.todoapp.ui.theme.TodoAppTheme
import com.fatherofapps.jnav.annotations.JNav

@Composable
@JNav(
    destination = "home_destination",
    baseRoute = "home_route",
    name = "HomeNavigation"
)
fun HomeScreen(
    context: Context,
    onUpdate: (String) -> Unit,
    onDelete: (String) -> Unit,
    onModify: (Task) -> Unit,
    state: State
) {
    TodoAppTheme {

        var task by remember {
            mutableStateOf("")
        }
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp)
                .background(color = Color.White),

            ) {
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "To-Do List", style = MaterialTheme.typography.headlineLarge.copy(
                    color = Color(0xFF0b2964), fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
            TextField(value = task,
                onValueChange = { task = it },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(60.dp)
                    .border(width = 2.dp, color = Color.White, shape = RoundedCornerShape(60.dp))
                    .clip(shape = RoundedCornerShape(60.dp))
                    .background(color = Color(0xFFe1e2ec)),
                trailingIcon = {
                    Button(
                        onClick = {
                            if (task.isNotEmpty()) {
                                onUpdate(task)
                                task = ""
                            } else
                                Toast.makeText(context, "Hãy nhập Task cần làm", Toast.LENGTH_SHORT)
                                    .show()
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
                })
            Spacer(modifier = Modifier.height(24.dp))

            LoadingDialog(state = state)
            Lazy(state = state, onDelete = onDelete, onModify = onModify)
        }
    }
}

@Composable
fun Lazy(
    state: State,
    onModify: (Task) -> Unit,
    onDelete: (String) -> Unit
) {
    var tmp by remember {
        mutableStateOf(Task())
    }
    var task by remember {
        mutableStateOf("")
    }
    val listState = rememberLazyListState()
    if ((state.lazyState.value == LazyScreen.Updating) or (state.lazyState.value == LazyScreen.Updated)) {
        LazyColumn(state = listState) {
            items(items = state.taskList) { item ->
                Spacer(modifier = Modifier.height(24.dp))
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .background(
                            color = Color(0xFFe1e2ec),
                            shape = RoundedCornerShape(20.dp)
                        )
                ) {
                    val (text, button) = createRefs()
                    Text(text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xFF0b2964),
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                        { append(item.title) }
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xFF0b2964),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Light,
                                fontStyle = FontStyle.Italic
                            )
                        )
                        { append("\nCreated At : ${item.createdAt}") }
                    }, modifier = Modifier.constrainAs(text) {
                        start.linkTo(parent.start, margin = 10.dp)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    })
                    Column(modifier = Modifier.constrainAs(button) {
                        end.linkTo(parent.end, margin = 10.dp)
                    }) {
                        Button(
                            onClick = {
                                tmp = Task(item.taskId,item.title,item.createdAt)
                                task = item.title
                                state.openDialog.value = true
                            },
                            colors = ButtonColors(
                                containerColor = Color(0xFFff653e),
                                contentColor = Color.White,
                                disabledContentColor = Color(0xFFff653e),
                                disabledContainerColor = Color(0xFFff653e)
                            )
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_modify),
                                contentDescription = null
                            )
                        }
                        Button(
                            onClick = {
                                onDelete(item.taskId)
                            },
                            colors = ButtonColors(
                                containerColor = Color(0xFFff653e),
                                contentColor = Color.White,
                                disabledContentColor = Color(0xFFff653e),
                                disabledContainerColor = Color(0xFFff653e)
                            )
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_del),
                                contentDescription = null
                            )
                        }
                    }
                }
            }

        }
        state.lazyState.value = LazyScreen.Updated
    }
    if (state.openDialog.value) {
        Dialog(onDismissRequest = {
            state.openDialog.value = false
        }) {
            Card(
                modifier = Modifier
                    .size(height = 200.dp, width = 400.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    TextField(value = task, onValueChange = { task = it })
                    Button(onClick = {
                        tmp.title = task
                        onModify(tmp)
                    }) {
                        Text(text = "Update Data")
                    }
                }
            }
        }
    }
}

