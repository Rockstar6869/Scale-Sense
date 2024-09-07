package com.ujjolch.masterapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel(): ViewModel(){
    // LiveData variables for Gender and DOB
    val gender: MutableLiveData<String> = MutableLiveData("")
    val dob: MutableLiveData<Long> = MutableLiveData(0L)

    // Function to update Gender
    fun updateGender(newGender: String) {
        gender.value = newGender
    }

    // Function to update DOB
    fun updateDOB(newDOB: Long) {
        dob.value = newDOB
    }

    // Function to reset Gender and DOB to default values
    fun resetValues() {
        gender.value = ""
        dob.value = 0L
    }
}