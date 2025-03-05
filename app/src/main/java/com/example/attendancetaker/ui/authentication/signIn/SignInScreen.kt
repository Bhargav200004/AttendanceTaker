package com.example.attendancetaker.ui.authentication.signIn

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SignInScreen(modifier: Modifier = Modifier) {

    val viewModel: SignInViewModel = hiltViewModel()

    val uiState by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
      Text(
          text = "Teacher Sign In",
          style = MaterialTheme.typography.headlineMedium,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.padding(bottom = 24.dp)
      )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.email,
                onValueChange = {
                    viewModel.onEvent(SignInEvent.OnEmailChange(it))
                },
                isError = uiState.isEmailError,
                supportingText = {
                    if(uiState.isEmailError)
                        Text(
                            text = "Email is wrong"
                        )
                },
                label = { Text(text = "Email Address") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email Icon") }
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.password,
                onValueChange = {
                    viewModel.onEvent(SignInEvent.OnPasswordChange(it))
                },
                isError = uiState.isPasswordError,
                supportingText = {
                    if(uiState.isPasswordError)
                        Text(
                            text = "Password is wrong"
                        )
                },
                label = { Text(text = "Password") },
                visualTransformation = if (uiState.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = { Icon(Icons.Default.Lock , contentDescription = "Password Icon") },
                trailingIcon = {
                    IconButton(onClick = {
                        viewModel.onEvent(SignInEvent.OnPasswordVisibleChange(uiState.passwordVisible))
                    }) {
                        Icon(
                            imageVector = if (uiState.isButtonVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (uiState.isButtonVisible) "Hide Password" else "Show Password"
                        )
                    }
                }
            )

            Button(
                onClick = {
                    viewModel.onEvent(SignInEvent.OnSubmitButtonClick)
                },
                enabled = uiState.isButtonVisible,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                if ( uiState.isLoading ) CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    color = Color.White
                )
                else Text(text = "Sign Up", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                AnnotatedButtons(
                    supportingText = "Not Have Account? ",
                    clickableText = "Sign Up",
                    onAnnotatedButtonClick = {}
                )

                AnnotatedButtons(
                    supportingText = "Forget ",
                    clickableText = "Password",
                    onAnnotatedButtonClick = {}
                )

            }
        }
    }
}

@Composable
fun AnnotatedButtons(
    supportingText: String,
    clickableText: String,
    onAnnotatedButtonClick : () -> Unit
) {
    Row (
        verticalAlignment = Alignment.CenterVertically
    ){

        Text(
            text = supportingText,
            fontSize = 14.sp
        )

        Text(
            modifier = Modifier.
            clickable {
                onAnnotatedButtonClick()
            },
            text = AnnotatedString(clickableText),
            style = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        )
    }
}