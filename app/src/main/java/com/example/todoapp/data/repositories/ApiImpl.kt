package com.example.todoapp.data.repositories

import com.example.todoapp.common.getCurrentTime
import com.example.todoapp.model.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ApiImpl @Inject constructor(
    private val database: FirebaseDatabase,
    private val auth: FirebaseAuth
) : Api {
    private lateinit var dbRef: DatabaseReference
    private var operationSuccessful: Boolean = false
    val taskList = mutableListOf<Task>()
    override suspend fun login(username: String, password: String): Boolean {
        operationSuccessful = false
        auth.signInWithEmailAndPassword(username, password)
            .addOnSuccessListener {
                operationSuccessful = true
                dbRef = database.getReference("Tasks").child(auth.currentUser!!.uid)
            }.await()
        return operationSuccessful
    }

    override suspend fun isAuthenticated(): Boolean {
        operationSuccessful = false
        if (auth.currentUser != null) {
            dbRef = database.getReference("Tasks").child(auth.currentUser!!.uid)
            operationSuccessful = true
        }
        return operationSuccessful
    }

    override suspend fun getAuthState(): Boolean {
        return true
    }

    override suspend fun signup(username: String, password: String, cpwd: String): Boolean {
        if (password != cpwd)
            throw Exception("Mat khau va mat khau xac nhan khong trung khop")
        operationSuccessful = false
        auth.createUserWithEmailAndPassword(username, password)
            .addOnSuccessListener {
                operationSuccessful = true
                dbRef = database.getReference("Tasks").child(auth.currentUser!!.uid)
            }.await()
        return operationSuccessful

    }

    override suspend fun loadTasks(): List<Task> = suspendCoroutine { continuation ->
        taskList.clear()
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    if (dataSnapshot.exists()) {
                        for (task in dataSnapshot.children) {
                            @Suppress("NAME_SHADOWING") val task = task.getValue(Task::class.java)
                            task?.let { taskList.add(it) }
                        }
                    }
                    continuation.resume(taskList) // Trả kết quả khi hoàn tất
                } catch (e: Exception) {
                    continuation.resumeWithException(e) // Trả lỗi nếu có
                }
            }

            override fun onCancelled(error: DatabaseError) {
                continuation.resumeWithException(error.toException()) // Trả lỗi từ Firebase
            }
        })
    }


    override suspend fun addTask(title: String) {
        val key = dbRef.push().key!!
        val task = Task(key, title, getCurrentTime())
        dbRef.child(key).setValue(task).addOnSuccessListener {
        }.await()
    }

    override suspend fun updateTask(taskId: String, title: String, createdDate: String,isCompleted: Boolean) {
        dbRef.child(taskId).setValue(Task(taskId, title, createdDate,isCompleted)).addOnSuccessListener {}
            .await()
    }

    override suspend fun delTask(taskId: String) {
        dbRef.child(taskId).removeValue()
            .addOnSuccessListener {}.await()
    }
}