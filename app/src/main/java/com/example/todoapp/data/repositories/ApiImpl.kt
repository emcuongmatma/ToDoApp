package com.example.todoapp.data.repositories

import android.content.ContentValues.TAG
import android.util.Log
import com.example.todoapp.common.getCurrentTime
import com.example.todoapp.data.Result
import com.example.todoapp.model.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ApiImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : Api {
    override suspend fun login(username: String, password: String): Result<Boolean> = try {
        Result.Loading(true)
        auth.signInWithEmailAndPassword(username, password).await()
        Result.Success(true)
    } catch (e: Exception) {
        Result.Error(e.toString())
    }
    override suspend fun signup(username: String, password: String, cpwd: String): Result<Boolean> =
        try {
            if (password != cpwd)
                throw Exception("Mật khẩu và xác nhận mật khẩu không trùng khớp !")
            auth.createUserWithEmailAndPassword(username, password).await()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }

    override suspend fun getCurrentUser(): Result<String> = try {
        val userId = auth.currentUser!!.email
        if (userId != null) {
            Result.Success(userId)
        } else {
            Result.Error("User not authenticated")
        }
    } catch (e: Exception) {
        Result.Error(e.toString())
    }
    override fun loadTasks(username: String): Flow<List<Task>> = callbackFlow {
        val sub = firestore.collection("tasks").document(username).collection("my_task")
            .orderBy("createdAt")
            .addSnapshotListener { query, _ ->
                query?.let {
                    trySend(it.documents.map { documentSnapshot ->
                        documentSnapshot.toObject(Task::class.java)!!
                    }).isSuccess
                }
            }
        awaitClose { sub.remove() }
    }

    override suspend fun addTask(title: String, username: String): Result<Unit> = try {
        val task = Task(title = title, createdAt = getCurrentTime())
        val query =
            firestore.collection("tasks").document(username).collection("my_task").add(task).await()
        firestore.collection("tasks").document(username).collection("my_task").document(query.id)
            .update("taskId", query.id)
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e.message.toString())
    }


    override suspend fun updateTask(
        task: Task,
        username: String
    ) = try {
        firestore.collection("tasks").document(username).collection("my_task").document(task.taskId)
            .set(task)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e.message.toString())
    }

    override suspend fun delTask(taskId: String, username: String) = try {
        firestore.collection("tasks").document(username).collection("my_task").document(taskId)
            .delete()
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e.message.toString())
    }
}