package com.example.attendancetaker.ui.authentication.signIn

data class SignInData(
    val name : String = "",
    val email : String = "",
    val password : String = "",
    val passwordVisible : Boolean = false,
)