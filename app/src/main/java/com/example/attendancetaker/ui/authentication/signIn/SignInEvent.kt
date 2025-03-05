package com.example.attendancetaker.ui.authentication.signIn


sealed class SignInEvent {

    data class OnEmailChange(val email : String) : SignInEvent()

    data class OnPasswordChange(val password : String) :  SignInEvent()

    data class OnPasswordVisibleChange(val passwordVisibility : Boolean) : SignInEvent()

    data object OnSubmitButtonClick : SignInEvent()

}