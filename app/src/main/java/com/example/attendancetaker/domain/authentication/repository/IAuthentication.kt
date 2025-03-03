package com.example.attendancetaker.domain.authentication.repository

import io.github.jan.supabase.auth.user.UserInfo

interface IAuthentication {
    suspend fun signIn (email: String, password: String): Boolean
    suspend fun signUp (email: String, password: String): Boolean
    suspend fun isLoginUser(): Boolean
    suspend fun signOut(): Boolean
    suspend fun getAuthToken() : String?
}