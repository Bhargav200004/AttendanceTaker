package com.example.attendancetaker.ui.authentication.signUp


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendancetaker.MySharedPreferenceDataStore
import com.example.attendancetaker.repository.AuthenticationImpl
import com.example.attendancetaker.utils.Result
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
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpAuth: AuthenticationImpl,
    private val preferenceDataStore: MySharedPreferenceDataStore,
) : ViewModel() {

    private var _state = MutableStateFlow(SignUpData())
    val state = _state.asStateFlow().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = SignUpData()
    )

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.OnNameChange -> onNameChange(name = event.name)
            is SignUpEvent.OnEmailChange -> onEmailChange(email = event.email)
            is SignUpEvent.OnPasswordChange -> onPasswordChange(password = event.password)
            is SignUpEvent.OnPasswordVisibleChange -> onPasswordVisibilityChange(event.passwordVisibility)
            SignUpEvent.OnSubmitButtonClick -> onSubmitButtonClick()
        }
    }

    private fun onNameChange(name: String) {
        viewModelScope.launch {
            _state.update { state ->
                state.copy(
                    name = name
                )
            }
        }
    }

    private fun onEmailChange(email: String) {
        viewModelScope.launch {
            _state.update { state ->
                state.copy(
                    email = email
                )
            }
            if (isValidEmail(email)) {
                _state.update { state ->
                    state.copy(
                        isEmailError = false,
                        isButtonVisible = true
                    )
                }
            } else {
                _state.update { state ->
                    state.copy(
                        isEmailError = true,
                        isButtonVisible = false
                    )
                }
            }
        }
    }

    private fun onPasswordChange(password: String) {
        viewModelScope.launch {
            _state.update { state ->
                state.copy(
                    password = password
                )
            }
            if (isValidPassword(password)) {
                _state.update { state ->
                    state.copy(
                        isPasswordError = false,
                        isButtonVisible = true
                    )
                }
            } else {
                _state.update { state ->
                    state.copy(
                        isPasswordError = true,
                        isButtonVisible = false
                    )
                }
            }
        }
    }

    private fun onPasswordVisibilityChange(passwordVisible: Boolean) {
        viewModelScope.launch {
            _state.update { state ->
                state.copy(
                    passwordVisible = !passwordVisible
                )
            }
        }
    }

    private fun onSubmitButtonClick() {
        viewModelScope.launch {
            try {

                if (!state.value.isButtonVisible) return@launch
                signUp()

            } catch (e: Exception) {

                _state.update { state ->
                    state.copy(
                        navigationApproval = false
                    )
                }
                Timber.e("onSubmitButtonClick: ${e.message}")

            }
        }
    }

    private suspend fun signUp() {
        try {

            _state.update { state ->
                state.copy(
                    isLoading = true
                )
            }
            val result = signUpAuth.signUp(
                email = state.value.email,
                password = state.value.password
            )

            when (result) {

                is Result.OnSuccess -> {
                    SnackBarController.sendEvent(
                        event = SnackBarEvent(
                            message = result.data?.successMessage ?: ""
                        )
                    )
                    preferenceDataStore.onSendTeacherName(teacherName = state.value.name)
                    _state.update { state ->
                        state.copy(
                            isLoading = false,
                            navigationApproval = true,
                        )
                    }

                }

                is Result.OnError -> {

                    _state.update { state ->
                        state.copy(
                            isLoading = false,
                            navigationApproval = false
                        )
                    }
                    SnackBarController.sendEvent(
                        event = SnackBarEvent(
                            message = result.error?.errorMessage?.errorDescription ?: "Try Again"
                        )
                    )
                }
            }
        } catch (e: Exception) {

            _state.update { state ->
                state.copy(
                    navigationApproval = false,
                    isLoading = false
                )
            }
            SnackBarController.sendEvent(
                event = SnackBarEvent(
                    message = "No Internet Connection"
                )
            )

            Timber.d("signUpError: ${e.message}")

        }
    }
}

