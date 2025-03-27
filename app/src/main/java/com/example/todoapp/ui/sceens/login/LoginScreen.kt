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
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.todoapp.data.Result
import com.example.todoapp.ui.sceens.home.HomeNavigation
import com.example.todoapp.ui.theme.darkBlue
import com.example.todoapp.ui.theme.lightBlue
import com.example.todoapp.ui.theme.whiteBackground
import com.fatherofapps.jnav.annotations.JNav
import com.fatherofapps.jnav.annotations.JNavArg

@Composable
@JNav(
    destination = "login_destination",
    baseRoute = "login_route",
    name = "LoginNavigation",
    arguments = [
        JNavArg(
            name = "email",
            type = String::class,
            isNullable = true
        )
    ]
)
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel,
    mainViewModel: MainViewModel
) {
    val state = viewModel.uiState.collectAsState()
    var isError by remember {
        mutableStateOf(false)
    }
    val status = viewModel.authResult.observeAsState()
    val focusManager = LocalFocusManager.current
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    Scaffold { paddingValue ->
        Column(
            modifier = Modifier
                .padding(paddingValue)
                .background(
                    color = if (isSystemInDarkTheme()) lightBlue else whiteBackground
                )
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = if (!isSystemInDarkTheme()) lightBlue else whiteBackground,
                    fontWeight = FontWeight.Bold,
                ),
            )
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(
                value = state.value.username,
                onValueChange = { viewModel.updateUsername(it) },
                label = { Text(text = "Type your email") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                isError = isError,
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
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                isError = isError,
                trailingIcon = {
                    val description = if (passwordVisible) "Hide password" else "Show password"
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = Icons.Default.Info, description)
                    }
                },
                shape = RoundedCornerShape(24.dp),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Not Registered ?")
                TextButton(onClick = {
                    navController.navigate(SignUpNavigation.createRoute(state.value.username))
                }) {
                    Text(text = "Sign Up")
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    if ((state.value.username.isNotEmpty()) and (state.value.password.isNotEmpty())) {
                        viewModel.login()
                    } else
                        isError = true
                }, colors = ButtonColors(
                    contentColor = Color.White,
                    containerColor = darkBlue,
                    disabledContainerColor = darkBlue,
                    disabledContentColor = Color.White
                )
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, tint = Color.White)
            }
            if (state.value.status is LoadStatus.Loading){
                CircularProgressIndicator()
            }
            when (status.value) {
                is Result.Success -> {
                    LaunchedEffect(Unit) {
                        navController.navigate(HomeNavigation.route) {
                            popUpTo(0)
                        }
                    }
                }
                is Result.Error -> {
                    mainViewModel.setError("Tài khoản hoặc mật khẩu không đúng")
                    viewModel.reset()
                }
                else -> {}
            }
        }
    }
}

