package com.example.room.mvvm.model
import com.google.gson.annotations.SerializedName


data class ApiResponseClass(
    @SerializedName("errorCode")
    val errorCode: String?,
    @SerializedName("errorMessage")
    val errorMessage: String?,
    @SerializedName("user")
    val user: User?
)

data class User(
    @SerializedName("userId")
    val userId: Int?,
    @SerializedName("userName")
    val userName: String?
)