package com.example.masterapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val userRepository: UserRepository
    init {
        userRepository= UserRepository(
            FirebaseAuth.getInstance(),
            Injection.instance()
        )
    }
    private val _authResult = MutableLiveData<Result<Boolean>>()
    val authResult: LiveData<Result<Boolean>> get() = _authResult

    val isLoading: StateFlow<Boolean> = userRepository.isLoading.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )


    val isLoggedIn: StateFlow<Boolean> = userRepository.isLoggedIn.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    fun signUp(email: String, password: String, firstName: String, lastName: String) {
        viewModelScope.launch {
            _authResult.value = userRepository.SignUp(firstName,lastName,password,email)
        }
    }
    fun LogIn(email: String,password:String){
        viewModelScope.launch {
            _authResult.value = userRepository.LogIn(email,password)
        }
    }
    fun LogOut(){
           _authResult.value = userRepository.LogOut()
    }
    fun resetAuthResult() {
        _authResult.value = null
    }
}