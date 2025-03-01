package com.example.attendancetaker.ui.authentication.signIn


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendancetaker.repository.AuthenticationImpl
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
    private val signInAuth : AuthenticationImpl
)  : ViewModel() {

    private var _state = MutableStateFlow(SignInData())
    val state = _state.asStateFlow().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = SignInData()
    )

    fun onEvent(event: SignInEvent) {
        when(event){
            is SignInEvent.OnNameChange -> onNameChange(name = event.name)
            is SignInEvent.OnEmailChange -> onEmailChange(email = event.email)
            is SignInEvent.OnPasswordChange -> onPasswordChange(password = event.password)
            is SignInEvent.OnPasswordVisibleChange -> onPasswordVisibilityChange(event.passwordVisibility)
            SignInEvent.OnSubmitButtonClick -> TODO()
        }
    }

    private fun onNameChange (name: String) {
        viewModelScope.launch {
            _state.update {state ->
                state.copy(
                    name = name
                )
            }
        }
    }

    private fun onEmailChange (email: String) {
        viewModelScope.launch {
            _state.update {state ->
                state.copy(
                    email = email
                )
            }
        }
    }

    private fun onPasswordChange (password: String) {
        viewModelScope.launch {
            _state.update { state ->
                state.copy(
                    password = password
                )
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

}