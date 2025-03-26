package com.example.todoapp

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todoapp.ui.sceens.home.HomeNavigation
import com.example.todoapp.ui.sceens.home.HomeScreen
import com.example.todoapp.ui.sceens.login.LoginNavigation
import com.example.todoapp.ui.sceens.login.LoginScreen
import com.example.todoapp.ui.sceens.login.SignUpNavigation
import com.example.todoapp.ui.sceens.login.SignUpScreen

@Composable
fun Navigation(
    startDestination:String
) {
    val navController = rememberNavController()
    val mainViewModel: MainViewModel = hiltViewModel()
    val mainState = mainViewModel.uiState.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(mainState.value.error) {
        if (mainState.value.error != "") {
            Toast.makeText(context, mainState.value.error, Toast.LENGTH_SHORT).show()
            mainViewModel.setError("")
        }
    }
    NavHost(navController = navController, startDestination = startDestination) {
        composable(HomeNavigation.route) {
            HomeScreen(viewModel= hiltViewModel(),mainViewModel = mainViewModel)
        }
        composable(LoginNavigation.route) {
            LoginScreen(navController, viewModel = hiltViewModel(), mainViewModel = mainViewModel)
        }
        composable(SignUpNavigation.route, arguments = SignUpNavigation.arguments()) {
            SignUpScreen(
                navController = navController,
                tmpemail = SignUpNavigation.email(it).toString(),
                viewModel = hiltViewModel(),
                mainViewModel = mainViewModel
            )
        }
    }
}