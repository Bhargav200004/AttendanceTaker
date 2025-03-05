package com.example.attendancetaker.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.example.attendancetaker.MySharedPreferenceDataStore
import com.example.attendancetaker.domain.authentication.repository.IAuthentication
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import javax.inject.Inject

class AuthenticationImpl @Inject constructor(
    private val auth: Auth,
    private val sharedPreferenceDataStore: MySharedPreferenceDataStore
): IAuthentication {

    // Sign In Function
    override suspend fun signIn(email: String, password: String) : Boolean {
        return try
        {
            auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            true
        }
        catch (e: Exception)
        {
            Log.e(TAG , "error => ${e.message}")
            false
        }
    }

    // Sign Up Function
    override suspend fun signUp(email: String, password: String): Boolean {
        return try
        {
              auth.signUpWith(Email){
                this.email = email
                this.password = password
            }
            true
        }
        catch (e : Exception)
        {
            Log.e(TAG , "error => ${e.message}")
            false
        }
    }

    // Is Login User Function
    override suspend fun isLoginUser(): Boolean {
        return try {
            val token = getAuthToken()
            if (token == null )  return false
            else {
                auth.refreshCurrentSession()
                val newAuthToken = getAuthToken() ?: return false
                val teacherDetails = auth.retrieveUser(newAuthToken)
                sharedPreferenceDataStore.onSendTokenUserId(
                    userToken = newAuthToken,
                    teacherId = teacherDetails.id
                )
                return true
            }

        }
        catch (e : Exception){
            Log.e(TAG , "${e.message}")
            false
        }
    }

    // Sign Out Function
    override suspend fun signOut(): Boolean {
        return try
        {
            auth.signOut()
            true
        }
        catch (e : Exception)
        {
            false
        }
    }

    // Getting Auth Token
    override suspend fun getAuthToken() : String? {
        return try {
            auth.currentAccessTokenOrNull()
        }
        catch (e : Exception){
            Log.e(TAG , "error => ${e.message}")
            null
        }
    }

    // Getting Teacher Details
    override suspend fun getTeacherDetails(): UserInfo? {
        return try {
            val authToken = getAuthToken() ?: return null
            auth.retrieveUser(authToken)
        }catch (e : Exception){
            Log.e(TAG , "error => ${e.message}")
            null
        }
    }

}