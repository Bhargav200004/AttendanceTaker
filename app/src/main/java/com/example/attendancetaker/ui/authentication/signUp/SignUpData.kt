package com.example.attendancetaker.ui.authentication.signUp

data class SignUpData(
    val name : String = "",
    val email : String = "",
    val password : String = "",
    val passwordVisible : Boolean = false,
    val isPasswordError : Boolean = false,
    val isEmailError : Boolean = false,
    val isButtonVisible : Boolean = false,
    val isLoading : Boolean = false,
    val navigationApproval : Boolean = false
)