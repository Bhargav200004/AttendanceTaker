package com.example.attendancetaker.repository

import android.util.Log
import com.example.attendancetaker.domain.authentication.repository.IAuthentication
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email
import javax.inject.Inject

class AuthenticationImpl @Inject constructor(
    private val auth: Auth
): IAuthentication {

    // Sign In Function
    override suspend fun signIn(email: String, password: String): Boolean {
        return try
        {
            val result = auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            Log.e("SignIn Details" , result.toString())
            true
        }
        catch (e: Exception)
        {
            false
        }
    }

    // Sign Up Function
    override suspend fun signUp(email: String, password: String): Boolean {
        return try
        {
            val result = auth.signUpWith(Email){
                this.email = email
                this.password = password
            }
            Log.e("SignUp Details" , result.toString())
            true
        }
        catch (e : Exception)
        {
            false
        }
    }
}