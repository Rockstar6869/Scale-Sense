package com.example.masterapp

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await


class UserRepository( private val auth:FirebaseAuth,
                      private val firestore: FirebaseFirestore
){
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _isLoggedIn = MutableStateFlow(auth.currentUser != null)
    val isLoggedIn: StateFlow<Boolean> get() = _isLoggedIn

    init {
        auth.addAuthStateListener { firebaseAuth ->
            _isLoggedIn.value = firebaseAuth.currentUser != null
            _isLoading.value = false
        }
    }


    suspend fun SignUp(firstName:String,lastName:String,Password:String,
                       email:String): Result<Boolean> =
        try {
            //add user to firestore
            auth.createUserWithEmailAndPassword(email,Password).await() //This line adds user in the authentication tab
            val user = User(firstName,lastName,email) //This is for adding the user data in the 'users' collection
            SaveusertoFirestore(user)
            Result.Success(true)
        } catch (e:Exception){
            Result.Error(e)
        }
    private suspend fun SaveusertoFirestore(user: User){
        firestore.collection("users").document(user.email).
        set(user).await()
    }
    suspend fun LogIn(email:String,
                      password:String): Result<Boolean> =
        try {
            auth.signInWithEmailAndPassword(email,password).await()
            Result.Success(true)
        }
        catch (e:Exception){
            Result.Error(e)
        }
    suspend fun loadcurrentuser(): Result<User> =
        try {
            val uid = auth.currentUser?.email
            if (uid != null) {
                val userDocument = firestore.collection("users").document(uid).get().await()
                val user = userDocument.toObject(User::class.java)
                if (user != null) {
                    Log.d("user2","$user")
                    Result.Success(user)
                } else {
                    Result.Error(Exception("User data not found"))
                }
            } else {
                Result.Error(Exception("User not authenticated"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    fun LogOut(): Result<Boolean> =
        try {
            auth.signOut()
            _isLoggedIn.value = false
            Result.Success(true)
        }
        catch(e:Exception){
            Result.Error(e)
        }


}