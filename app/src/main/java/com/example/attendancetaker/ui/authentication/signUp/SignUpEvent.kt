package com.example.attendancetaker.ui.authentication.signUp

sealed class SignUpEvent {

    data class OnNameChange(val name : String) : SignUpEvent()

    data class OnEmailChange(val email : String) : SignUpEvent()

    data class OnPasswordChange(val password : String) :  SignUpEvent()

    data class OnPasswordVisibleChange(val passwordVisibility : Boolean) : SignUpEvent()

    data object OnSubmitButtonClick : SignUpEvent()
}