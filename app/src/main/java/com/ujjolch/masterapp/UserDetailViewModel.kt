package com.ujjolch.masterapp

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
    private val _userData = MutableLiveData<UserData>().apply {
        // Observe changes in userData and update dob
        observeForever { userData ->
            if (userData != null) {
                _age.value = millisToAge(userData.dob)
            } else {
                _age.value = null  // Or handle null case if necessary
            }
        }
    }
    val userData: LiveData<UserData> get() = _userData
    private val _age = MutableLiveData<Int>()
    val age: LiveData<Int> get() = _age
    private val _userHist = MutableLiveData<List<hist>>()
    val userHist: LiveData<List<hist>> get() = _userHist
    private val _devices = MutableLiveData<List<themistoscale>>()
    val devices: LiveData<List<themistoscale>> get() = _devices
    private val _units = MutableLiveData<Units>()
    val units: LiveData<Units> get() = _units
    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> get() = _currentUser
    private val _currentSubUser = MutableLiveData<subuser>()
    val currentSubUser: LiveData<subuser> get() = _currentSubUser
    private val _SubUserList = MutableLiveData<List<subuser>>()
    val SubUserList: LiveData<List<subuser>> get() = _SubUserList

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
    fun addSubUser(subuser: subuser,ud:UserData){
        viewModelScope.launch {
            when(val result = UserDetailRepository.addSubUser(currentUser.value?.email?:"",subuser)){
                is Result.Success -> {
                    Log.d("CheckTag","${result.data}")
                        uploadDetailsForAddUser(ud,result.data) //result returns the uuid if Success
                        getSubUserList()
                }
                is Result.Error ->{

                }
            }
        }
    }

    fun setCurrentSubUser(subUser:subuser){
        viewModelScope.launch {
            _currentSubUser.value = subUser
            getCurrentSubUserData()
            gethistlist()
            updateUserData()
        }
    }

    fun getCurrentSubUserData(){  //gets both userdetails and userhist of sub user
        viewModelScope.launch {
            getUserData(currentUser.value?.email)
            gethistlist()
        }
    }

    fun getSubUserList(){  //Only for the first time when app is loaded. After that use updateSubUserList
        viewModelScope.launch {

                when(val result = UserDetailRepository.getSubUserList(currentUser.value?.email?:"")){
                    is Result.Success ->{
                        _SubUserList.value = result.data
                        _currentSubUser.value = SubUserList.value?.get(0)
                        gethistlist()
                        updateUserData()
                    }
                    is Result.Error ->{

                    }
                }
        }
    }
    fun updateSubUserList(){
        viewModelScope.launch {

            when(val result = UserDetailRepository.getSubUserList(currentUser.value?.email?:"")){
                is Result.Success ->{
                    _SubUserList.value = result.data
                    gethistlist()
                    updateUserData()
                }
                is Result.Error ->{

                }
            }
        }
    }


    fun uploadDetails(ud: UserData) {
        viewModelScope.launch {
            when (UserDetailRepository.uploadDetails(currentUser.value?.email?:"",ud,currentSubUser.value?.uuid?:"")) { //subuseruuid to be added
                is Result.Success -> {
                    getUserData(currentUser.value?.email)
                }
                is Result.Error -> {

                }
            }
        }

    }

    fun uploadDetailsForAddUser(ud: UserData,useruuid:String) {  //only for add user
        viewModelScope.launch {
            when (UserDetailRepository.uploadDetails(currentUser.value?.email?:"",ud,useruuid)) { //subuseruuid to be added
                is Result.Success -> {
                    getUserData(currentUser.value?.email)
                }
                is Result.Error -> {

                }
            }
        }

    }

    fun getUserData(mailId:String?){
        viewModelScope.launch {
            if(mailId != null){
                when(val result = UserDetailRepository.getUserData(mailId,currentSubUser.value?.uuid?:"")){   //subuseruuid to be added
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
                    getSubUserList()
                    getUnits()
                    Log.d("UjTag12","${currentUser.value?.email}")
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
                UserDetailRepository.updatehistlist(currentUser.value!!.email, newHist,currentSubUser.value?.uuid?:"")
            }
//            getweightlist()
        }
    }

    fun updateUserData() {
        viewModelScope.launch {
            getUserData(currentUser.value?.email)
        }
    }

    fun gethistlist(){
        viewModelScope.launch {
            when(val result = UserDetailRepository.gethistlist(currentUser.value?.email?:"",currentSubUser.value?.uuid?:"")){
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
    fun clearUserDataAndHist() {
        _userData.value = null
        _userHist.value = emptyList()
    }

    fun uploadUnit(ut:Units){
        viewModelScope.launch {
            when (UserDetailRepository.uploadUnit(currentUser.value?.email?:"",ut)) {
                is Result.Success -> {
                    getUnits()
                }
                is Result.Error -> {}
            }
        }

    }

    fun getUnits(){
        viewModelScope.launch {
                when(val result = UserDetailRepository.getUnits(currentUser.value?.email?:"")){
                    is Result.Success ->{
                        _units.value= result.data
                    }
                    is Result.Error -> {

                    }
                }

        }
    }


}