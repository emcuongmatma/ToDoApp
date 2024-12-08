package com.example.todoapp.ui.sceens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.todoapp.MainViewModel
import com.example.todoapp.common.enum.LoadStatus
import com.example.todoapp.ui.sceens.home.HomeNavigation
import com.fatherofapps.jnav.annotations.JNav
import com.fatherofapps.jnav.annotations.JNavArg

@Composable
@JNav(
    destination = "signup_destination",
    baseRoute = "signup_route",
    name = "SignUpNavigation",
    arguments = [JNavArg(
        name = "email", type = String::class, isNullable = true
    )]
)
fun SignUpScreen(
    navController: NavController,
    tmpemail: String,
    viewModel: LoginViewModel,
    mainViewModel: MainViewModel
) {
    val state = viewModel.uiState.collectAsState()
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var pwdCheck by remember {
        mutableStateOf(false)
    }
    val focusManager = LocalFocusManager.current
    LaunchedEffect(Unit) {
        viewModel.updateUsername(tmpemail)
    }
    Scaffold { paddingValue ->
        Column(
            modifier = Modifier
                .padding(paddingValue)
                .fillMaxSize()
                .background(color = if (isSystemInDarkTheme()) Color(0xFF0b2964) else Color(0xFFe1e2ec)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = if (!isSystemInDarkTheme()) Color(0xFF0b2964) else Color(0xFFe1e2ec),
                    fontWeight = FontWeight.Bold,
                ),
            )
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(
                value = state.value.username,
                onValueChange = { viewModel.updateUsername(it) },
                label = { Text(text = "Type your email") },
                isError = pwdCheck,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(24.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = state.value.password,
                onValueChange = { viewModel.updatePwd(it) },
                label = {
                    Text("Type your password")
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password, imeAction = ImeAction.Next
                ),
                trailingIcon = {
                    val description = if (passwordVisible) "Hide password" else "Show password"
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = Icons.Default.Info, description)
                    }
                },
                isError = pwdCheck,
                shape = RoundedCornerShape(24.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = state.value.confirmpwd,
                onValueChange = { viewModel.updatecpwd(it) },
                label = {
                    Text("Confirm password")
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                ),
                trailingIcon = {
                    val description = if (passwordVisible) "Hide password" else "Show password"
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = Icons.Default.Info, description)
                    }
                },
                isError = pwdCheck,
                shape = RoundedCornerShape(24.dp),
                keyboardActions = KeyboardActions(
                    onDone = {
                        viewModel.signup()
                        focusManager.clearFocus()
                    }
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Already Registered ?")
                TextButton(onClick = {
                    navController.popBackStack()
                }) {
                    Text(text = "Sign In")
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    if ((state.value.username.isNotEmpty()) and (state.value.password.isNotEmpty()) and (state.value.confirmpwd.isNotEmpty())) {
                        viewModel.signup()
                    } else pwdCheck = true
                }, colors = ButtonColors(
                    contentColor = Color.White,
                    containerColor = Color(0xFF0b2964),
                    disabledContainerColor = Color(0xFF0b2964),
                    disabledContentColor = Color.White
                )
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, tint = Color.White)
            }
            when (state.value.status) {
                is LoadStatus.Loading -> {
                    CircularProgressIndicator()
                }
                is LoadStatus.Success -> {
                    /// chuyen den home
                    LaunchedEffect(Unit) {
                        navController.navigate(HomeNavigation.route)
                    }
                }
                is LoadStatus.Error -> {
                    mainViewModel.setError(state.value.status.description)
                    viewModel.reset()
                }
                else -> {}
            }
        }
    }
}