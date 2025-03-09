package com.example.attendancetaker.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.example.attendancetaker.domain.authentication.models.AuthData
import com.example.attendancetaker.domain.authentication.repository.IAuthentication
import com.example.attendancetaker.domain.teacher.model.TeacherAuthDetails
import com.example.attendancetaker.utils.Result
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import javax.inject.Inject

class AuthenticationImpl @Inject constructor(
    private val auth: Auth,
): IAuthentication {


    // Sign Up Function
    override suspend fun signUp(email: String, password: String): Result<AuthData> {
        return try
        {
            auth.signUpWith(Email){
                this.email = email
                this.password = password
            }
            Result.OnSuccess(
                AuthData(
                    successMessage = "Successfully Register"
                )
            )
        }
        catch (e : AuthRestException)
        {
            Result.OnError(
                AuthData(
                    errorMessage = e
                )
            )
        }
    }

    // Sign In Function
    override suspend fun signIn(email: String, password: String) : Result<AuthData> {
        return try
        {
            auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            Result.OnSuccess(data = AuthData(
                successMessage = "Successfully Login \nEnjoy App"
            ))
        }
        catch (e: AuthRestException)
        {
            Result.OnError(
                error = AuthData(
                    errorMessage = e
                )
            )
        }
    }

    // Is Login User Function
    override suspend fun isLoginTeacher(): TeacherAuthDetails {
        return try {
            val token = getAuthToken()
            if (token == null )  return TeacherAuthDetails(isLogin = false)
            else {
                auth.refreshCurrentSession()
                val newAuthToken = getAuthToken() ?: return TeacherAuthDetails(isLogin = false)
                val teacherDetails = auth.retrieveUser(newAuthToken)
                return TeacherAuthDetails(
                    userToken = newAuthToken,
                    teacherId = teacherDetails.id,
                    isLogin = true
                )
            }
        }
        catch (e : Exception){
            Log.e(TAG , "${e.message}")
            return TeacherAuthDetails(isLogin = false)
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