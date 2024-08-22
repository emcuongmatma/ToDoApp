package com.example.todoapp
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todoapp.common.getCurrentTime
import com.example.todoapp.data.model.Task
import com.example.todoapp.home.HomeNavigation
import com.example.todoapp.home.HomeScreen
import com.example.todoapp.login.SignInNavigation
import com.example.todoapp.login.SignInScreen
import com.example.todoapp.login.SignUpNavigation
import com.example.todoapp.login.SignUpScreen
import com.example.todoapp.state.LazyScreen
import com.example.todoapp.state.State
import com.example.todoapp.ui.theme.TodoAppTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            TodoAppTheme {
                MainApp(LocalContext.current)
            }
        }
    }
}

@Composable
fun MainApp(
    context: Context,
    state: State = State()
) {
    val startDestination: String
    val navController = rememberNavController()
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    var dbRef = FirebaseDatabase.getInstance().getReference("Tasks")
    if (auth.currentUser!=null){
        startDestination = HomeNavigation.route
        dbRef=dbRef.child(auth.currentUser!!.uid)
        getList(context, dbRef, state)
    }else{
        startDestination = SignInNavigation.route
    }
    TodoAppTheme {

        NavHost(navController = navController, startDestination = startDestination) {
            composable(route = SignInNavigation.route) {

                SignInScreen(
                    onSignUp = { email ->
                        navController.navigate(SignUpNavigation.createRoute(email))
                    },
                    onSignIn = { name, pwd ->
                        state.loading.value=true
                        auth.signInWithEmailAndPassword(name, pwd)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_SHORT)
                                    .show()
                                state.loading.value=false
                                dbRef=dbRef.child(auth.currentUser!!.uid)
                                getList(context, dbRef, state)
                                navController.navigate(HomeNavigation.route){
                                    popUpTo(SignInNavigation.route) {
                                        inclusive = true
                                    }
                                }
                            }
                            .addOnFailureListener { err ->
                                state.loading.value=false
                                Toast.makeText(
                                    context,
                                    "Tài khoản hoặc mật khẩu không đúng ($err)",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    },
                    state)
            }
            composable(route = SignUpNavigation.route, arguments = SignUpNavigation.arguments()) {
                SignUpScreen(
                    context,
                    tmpemail = SignUpNavigation.email(it).toString(),
                    onSignUp = { name, pwd ->
                        state.loading.value=true
                        auth.createUserWithEmailAndPassword(name, pwd)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    context,
                                    "Tạo tài khoản thành công",
                                    Toast.LENGTH_SHORT
                                ).show()
                                state.loading.value=false
                            }
                            .addOnFailureListener { err ->
                                state.loading.value=false
                                Toast.makeText(context, "Thất bại : $err", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    },
                    onSignIn = {
                        navController.popBackStack()
                    },
                    loadingState = state
                )
            }
            composable(route = HomeNavigation.route) {
                HomeScreen(
                    context,
                    onUpdate = {
                        task->
                        getList(context, dbRef, state)
                        state.lazyState.value = LazyScreen.Updating
                        state.loading.value=true
                        val key = dbRef.push().key!!
                        val tmp = Task(key,task, getCurrentTime())
                        dbRef.child(key).setValue(tmp)
                            .addOnSuccessListener {
                                state.loading.value=false
                                Toast.makeText(
                                    context,
                                    "Thêm task thành công",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .addOnFailureListener {
                                err->
                                state.loading.value=false
                                Toast.makeText(
                                    context,
                                    "Lỗi $err",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    },
                    state = state,
                    onDelete = {
                        id->
                        val delete = FirebaseDatabase.getInstance().reference.child("Tasks").child(
                            auth.currentUser!!.uid).child(id)
                        delete.removeValue()
                            .addOnSuccessListener {
                                Toast.makeText(context,"Xoa thanh cong",Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                err->
                                Toast.makeText(context,"$err",Toast.LENGTH_SHORT).show()
                            }
                    },
                    onModify = {
                        item->
                        val modify = FirebaseDatabase.getInstance().reference.child("Tasks").child(
                            auth.currentUser!!.uid).child(item.taskId)
                        modify.setValue(item)
                            .addOnSuccessListener {
                                state.openDialog.value = false
                                Toast.makeText(context,"Chinh sua thanh cong",Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                    err->
                                state.openDialog.value = false
                                Toast.makeText(context,"$err",Toast.LENGTH_SHORT).show()
                            }
                    }
                )

            }
        }
    }
}
fun getList(context: Context, dbRef: DatabaseReference, state: State) {
    dbRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            state.taskList.clear()
            if (snapshot.exists()) {
                for (taskSnap in snapshot.children) {
                    @Suppress("NAME_SHADOWING") val taskSnap = taskSnap.getValue(Task::class.java)
                    state.taskList.add(taskSnap!!)
                }

            } else {
                Toast.makeText(context, "CSDL trong", Toast.LENGTH_SHORT).show()
            }
            state.lazyState.value = LazyScreen.Updating
        }
        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }
    })
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    TodoAppTheme {
        MainApp(context = LocalContext.current)
    }
}