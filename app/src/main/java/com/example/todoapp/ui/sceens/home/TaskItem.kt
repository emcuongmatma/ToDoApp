package com.example.todoapp.ui.sceens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.todoapp.R
import com.example.todoapp.model.Task

@Composable
fun TaskItem(
    task: Task,
    onEditClick: () -> Unit,
    onDeleteClick: (String) -> Unit,
    onToggleDone: (Boolean) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .background(
                color = Color(0xFFe1e2ec),
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        val (btnCheck, text, btn) = createRefs()
        RadioButton(
            selected = task.isCompleted,
            onClick = { onToggleDone(!task.isCompleted) },
            modifier = Modifier.constrainAs(btnCheck) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
        )
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color(0xFF0b2964),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Medium,
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    )
                ) {
                    append(task.title)
                }
                withStyle(
                    style = SpanStyle(
                        color = Color(0xFF0b2964),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Light,
                        fontStyle = FontStyle.Italic,
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    )
                ) {
                    append("\nCreated At: ${task.createdAt}")
                }
            },
            modifier = Modifier.constrainAs(text) {
                start.linkTo(btnCheck.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(btn.start)
            }
        )
        Column(
            modifier = Modifier.constrainAs(btn) {
                end.linkTo(parent.end, margin = 10.dp)
            }
        ) {
            Button(
                onClick = { onEditClick() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFff653e),
                    contentColor = Color.White
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_modify),
                    contentDescription = null
                )
            }
            Button(
                onClick = { onDeleteClick(task.taskId) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFff653e),
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}


