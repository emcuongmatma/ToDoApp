package com.example.todoapp.login

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoapp.state.LoadingDialog
import com.example.todoapp.state.State
import com.fatherofapps.jnav.annotations.JNav
import com.fatherofapps.jnav.annotations.JNavArg

@Composable
@JNav(
    destination = "signup_destination",
    baseRoute = "signup_route",
    name = "SignUpNavigation",
    arguments = [
        JNavArg(
            name = "email",
            type = String::class,
            isNullable = true
        )
    ]
)
fun SignUpScreen(
    context: Context,
    tmpemail: String,
    onSignIn: () -> Unit,
    onSignUp: (String, String) -> Unit,
    loadingState: State
) {
    var email by remember {
        mutableStateOf(tmpemail)
    }
    var pwd by remember {
        mutableStateOf("")
    }
    var cpwd by remember {
        mutableStateOf("")
    }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var pwdCheck by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sign Up",

            style = MaterialTheme.typography.headlineLarge.copy(
                color = Color(0xFF0b2964),
                fontWeight = FontWeight.Bold,

                ),

            )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Type your email") },
            isError = pwdCheck,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            shape = RoundedCornerShape(24.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = pwd, onValueChange = { pwd = it }, label = {
            Text("Type your password")
        },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            trailingIcon = {
                // Please provide localized description for accessibility services
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = Icons.Default.Info, description)
                }
            },
            isError = pwdCheck,
            shape = RoundedCornerShape(24.dp))
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = cpwd, onValueChange = { cpwd = it }, label = {
            Text("Confirm password")
        },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done),
            trailingIcon = {
                // Please provide localized description for accessibility services
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = Icons.Default.Info, description)
                }
            },
            isError = pwdCheck,
            shape = RoundedCornerShape(24.dp))
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Already Registered ?")
            TextButton(onClick = {
                onSignIn()
            }) {
                Text(text = "Sign In")
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                if ((email.isNotEmpty()) and (pwd.isNotEmpty()) and (cpwd.isNotEmpty())) {
                    if (pwd != cpwd) {
                        pwd = ""
                        cpwd = ""
                        Toast.makeText(
                            context,
                            "Mật khẩu và mật khẩu xác nhận không trùng khớp",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        onSignUp(email, pwd)
                    }
                }
                else
                    pwdCheck=true

            }, colors = ButtonColors(
                contentColor = Color.White,
                containerColor = Color(0xFF0b2964),
                disabledContainerColor = Color(0xFF0b2964),
                disabledContentColor = Color.White
            )
        ) {
            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, tint = Color.White)
        }
        LoadingDialog(state = loadingState)
    }
}

@Preview(showSystemUi = true)
@Composable
fun Pre() {

}