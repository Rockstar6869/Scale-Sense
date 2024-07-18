package com.example.masterapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class UserDetailsViewModel: ViewModel() {
    private val UserDetailRepository: UserDetailRepository
    private val userRepository: UserRepository
    private val _userData = MutableLiveData<UserData>()
    val userData: LiveData<UserData> get() = _userData
    private val _userHist = MutableLiveData<List<hist>>()
    val userHist: LiveData<List<hist>> get() = _userHist
    private val _devices = MutableLiveData<List<themistoscale>>()
    val devices: LiveData<List<themistoscale>> get() = _devices
    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> get() = _currentUser

    init{
        UserDetailRepository = UserDetailRepository(Injection.instance())
        userRepository = UserRepository(
            auth = FirebaseAuth.getInstance(),
            Injection.instance()
        )
        viewModelScope.launch {
            loadcurrentuser()
        }
    }
    fun uploadDetails(ud: UserData) {
        viewModelScope.launch {
            when (UserDetailRepository.uploadDetails(currentUser.value?.email?:"",ud)) {
                is Result.Success -> {
                    getUserData(currentUser.value?.email)
                }
                is Result.Error -> {}
            }
        }

    }
    fun getUserData(mailId:String?){
        viewModelScope.launch {
            if(mailId != null){
                when(val result = UserDetailRepository.getUserData(mailId)){
                    is Result.Success ->{
                        _userData.value= result.data
                        Log.d("UjTag3","${userData.value?.heightincm}")
                    }
                    is Result.Error -> {
                        Log.d("UjTag31","${result.exception}")
                    }
                }
            }

        }
    }

    suspend fun loadcurrentuser() {
        viewModelScope.launch {
            when (val result = userRepository.loadcurrentuser()) {
                is Result.Success -> {
                    _currentUser.value = result.data
                    getUserData(currentUser.value?.email)
                    Log.d("UjTag12","${currentUser.value?.email}")
                    gethistlist()
                    getDeviceList()
                    Log.d("UjTag2","${currentUser.value?.email}")
                }
                is Result.Error -> {

                }

            }
        }
    }
    fun updatehistlist(newHist:hist){
        viewModelScope.launch {
            if(currentUser.value?.email!=null) {
                UserDetailRepository.updatehistlist(currentUser.value!!.email, newHist)
            }
//            getweightlist()
        }
    }

    fun gethistlist(){
        viewModelScope.launch {
            when(val result = UserDetailRepository.gethistlist(currentUser.value?.email?:"")){
                is Result.Success ->{
                    _userHist.value = result.data
                }
                is Result.Error ->{

                }
            }
        }
    }

    fun bindDevice(device:themistoscale){
        viewModelScope.launch {
            if(currentUser.value?.email!= null){
                UserDetailRepository.bindDevice(currentUser.value?.email?:"",device)
            }
        }
    }

    fun getDeviceList(){
        viewModelScope.launch {
            when (val result = UserDetailRepository.getDevices(currentUser.value?.email?:"")){
                is Result.Success ->{
                    _devices.value = result.data
                }
                is Result.Error ->{

                }

            }
        }


    }


}