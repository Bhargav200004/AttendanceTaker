package com.example.attendancetaker.ui.authentication.signIn

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendancetaker.MySharedPreferenceDataStore
import com.example.attendancetaker.repository.AuthenticationImpl
import com.example.attendancetaker.utils.SnackBarController
import com.example.attendancetaker.utils.SnackBarEvent
import com.example.attendancetaker.utils.functions.ValidationFunction.isValidEmail
import com.example.attendancetaker.utils.functions.ValidationFunction.isValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInAuth : AuthenticationImpl,
    private val preferenceDataStore : MySharedPreferenceDataStore
) : ViewModel() {

    private var _state = MutableStateFlow(SignInData())
    val state = _state.asStateFlow().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = SignInData()
    )

    fun onEvent(event: SignInEvent){
        when(event){
            is SignInEvent.OnEmailChange -> onEmailChange(email = event.email)
            is SignInEvent.OnPasswordChange -> onPasswordChange(password = event.password)
            is SignInEvent.OnPasswordVisibleChange -> onPasswordVisibilityChange(passwordVisible = event.passwordVisibility)
            SignInEvent.OnSubmitButtonClick -> onSubmitButtonClick()
        }
    }

    private fun onEmailChange (email : String) {
        viewModelScope.launch {
            _state.update {state ->
                state.copy(
                    email = email
                )
            }
            if (isValidEmail(email)){
                _state.update { state ->
                    state.copy(
                        isEmailError = false,
                        isButtonVisible = true
                    )
                }
            }
            else{
                _state.update {state ->
                    state.copy(
                        isEmailError = true,
                        isButtonVisible = false
                    )
                }
            }
        }
    }

    private fun onPasswordVisibilityChange (passwordVisible: Boolean){
        viewModelScope.launch {
            _state.update {state ->
                state.copy(
                    passwordVisible = !passwordVisible
                )
            }
        }
    }

    private fun onPasswordChange (password : String){
        viewModelScope.launch {
            _state.update { state ->
                state.copy(
                    password = password
                )
            }
            if(isValidPassword(password)){
                _state.update {state ->
                    state.copy(
                        isPasswordError = false,
                        isButtonVisible = true
                    )
                }
            }
            else{
                _state.update {state ->
                    state.copy(
                        isPasswordError = true,
                        isButtonVisible = false
                    )
                }
            }
        }
    }

    private fun onSubmitButtonClick() {
        viewModelScope.launch {
            try {
                if (!state.value.isButtonVisible) return@launch
                _state.update { state ->
                    state.copy(
                        isLoading = true
                    )
                }
                signIn()
                _state.update { state ->
                    state.copy(
                        isLoading = false
                    )
                }
            }
            catch (e : Exception){
                Log.e(TAG,"error => ${e.message}")
            }
        }
    }

    private suspend fun signIn() {
        try {
            val result = signInAuth.signIn(
                email = state.value.email ,
                password = state.value.password
                )

            if (!result){
                _state.update {state ->
                    state.copy(
                        isLoading = false
                    )
                }
                SnackBarController.sendEvent(
                    event = SnackBarEvent(
                        message = "Not Register ! \nRetry after some time"
                    )
                )
            }

            val value = signInAuth.getAuthToken() ?: ""

            val teacherData = signInAuth.getTeacherDetails() ?: return

            preferenceDataStore.onSendTokenUserId(
                userToken =  value  ,
                teacherId = teacherData.id
                )

            SnackBarController.sendEvent(
                event = SnackBarEvent(
                    message = "Successfully Login Enjoy the app"
                )
            )

        }catch (e : Exception){
            SnackBarController.sendEvent(
                event = SnackBarEvent(
                    message = e.message.toString()
                )
            )
            Log.e(TAG , "error => ${e.message}")
        }
    }
}