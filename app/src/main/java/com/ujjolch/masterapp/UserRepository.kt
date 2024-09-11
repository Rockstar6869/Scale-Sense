package com.ujjolch.masterapp

import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID


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
            val user = User(firstName,lastName,email.lowercase()) //This is for adding the user data in the 'users' collection
            SaveusertoFirestore(user)
            Result.Success(true)
        } catch (e:Exception){
            Result.Error(e)
        }
    private suspend fun SaveusertoFirestore(user: User){
        firestore.collection("users").document(user.email).
        set(user).await()

        //To create a subUser profile of this user
        val docRef = firestore.collection("users").document(user.email).
        collection("subuser").document("subusers")

        val uuid = UUID.randomUUID().toString()
        val userUUID = user.firstName+uuid

        val userMap = mapOf(
            "firstname" to user.firstName,
            "lastname" to user.lastName,
            "uuid" to userUUID
        )
        docRef.set(mapOf("users" to listOf(userMap))).await()
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

    suspend fun changePassword(oldPassword: String, newPassword: String): Result<Boolean> =
        try {
            val user = auth.currentUser
            if (user != null) {
                val credential = EmailAuthProvider.getCredential(user.email!!, oldPassword)
                user.reauthenticate(credential).await() // Verify the old password
                user.updatePassword(newPassword).await() // Update to new password
                Result.Success(true)
            } else {
                Result.Error(Exception("User not authenticated"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }

    suspend fun sendVerificationEmail(): Result<Boolean> =
        try {
            val user = auth.currentUser
            user?.let {
                it.sendEmailVerification().await()
                Result.Success(true)
            } ?: Result.Error(Exception("User not authenticated"))
        } catch (e: Exception) {
            Result.Error(e)
        }

//    suspend fun isEmailVerified(): Result<Boolean> =
//        try {
//            val user = auth.currentUser
//            user?.reload()?.await() // Refresh the user to get the latest data
//            user?.let {
//                if (it.isEmailVerified) {
//                    Result.Success(true)
//                } else {
//                    Result.Success(false)
//                }
//            } ?: Result.Error(Exception("User not authenticated"))
//        } catch (e: Exception) {
//            Result.Error(e)
//        }
    suspend fun isEmailVerified(email: String): Result<Boolean> =
        try {
            val user = auth.currentUser

            // Check if the currently logged-in user's email matches the provided email
            if (user != null && user.email == email) {
                user.reload().await() // Refresh the user to get the latest data

                if (user.isEmailVerified) {
                    Result.Success(true)
                } else {
                    Result.Success(false)
                }
            } else {
                Result.Error(Exception("Email does not match the authenticated user"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    suspend fun firebaseAuthWithGoogle(idToken: String): Result<Boolean> = try {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val authResult = auth.signInWithCredential(credential).await()

        // Get user details from FirebaseUser
        val user = authResult.user
        val firstName = user?.displayName?.split(" ")?.firstOrNull() ?: ""
        val lastName = user?.displayName?.split(" ")?.lastOrNull() ?: ""
        val email = user?.email ?: ""
        val usertosave = User(firstName,lastName,email.lowercase())
        when(val result = checkIfFirstTimeUser()){
            is Result.Success ->{
                if(result.data == true){
                    androidx.media3.common.util.Log.d("FB125", "working")
                    SaveusertoFirestore(usertosave)
                }
                else{
                    androidx.media3.common.util.Log.d("FB125", "not working")
                }
            }

            is Result.Error -> {
                androidx.media3.common.util.Log.d("FB125", "Not working:${result.exception}")
            }
        }



        Result.Success(true)
    } catch (e: Exception) {
        androidx.media3.common.util.Log.d("FB125", "$e")
        Result.Error(e)
    }
}

