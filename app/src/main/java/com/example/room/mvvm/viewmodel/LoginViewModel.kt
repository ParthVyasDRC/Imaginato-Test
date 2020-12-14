package com.example.room.mvvm.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.room.mvvm.model.ApiResponseClass
import com.example.room.mvvm.model.LoginTableModel
import com.example.room.mvvm.repository.LoginRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class LoginViewModel : ViewModel() {

    var liveDataLogin: LiveData<LoginTableModel>? = null

    fun insertData(context: Context, username: String, password: String) {
       LoginRepository.insertData(context, username, password)
    }

    fun getLoginDetails(context: Context, username: String) : LiveData<LoginTableModel>? {
        liveDataLogin = LoginRepository.getLoginDetails(context, username)
        return liveDataLogin
    }

}