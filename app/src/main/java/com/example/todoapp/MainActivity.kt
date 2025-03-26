package com.example.todoapp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todoapp.ui.sceens.AuthenticationViewModel
import com.example.todoapp.ui.sceens.home.HomeNavigation
import com.example.todoapp.ui.sceens.login.LoginNavigation
import com.example.todoapp.ui.theme.TodoAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            TodoAppTheme {
                ToDoApp()
            }
        }
    }
}

@Composable
fun ToDoApp(
) {
    var startDestination: String= LoginNavigation.route
    val authViewModel : AuthenticationViewModel = hiltViewModel()
    val authState = authViewModel.uiState.collectAsState()
    if (authState.value.authStatus){
        startDestination = HomeNavigation.route
    }
    Navigation(startDestination)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    TodoAppTheme {

    }
}