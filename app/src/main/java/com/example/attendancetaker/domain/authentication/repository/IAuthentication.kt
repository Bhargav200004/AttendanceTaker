package com.example.attendancetaker.domain.authentication.repository

import com.example.attendancetaker.domain.authentication.models.AuthData
import com.example.attendancetaker.domain.teacher.model.TeacherAuthDetails
import io.github.jan.supabase.auth.user.UserInfo
import com.example.attendancetaker.utils.Result

interface IAuthentication {
    suspend fun signUp(email: String, password: String): Result<AuthData>
    suspend fun signIn (email: String, password: String): Result<AuthData>
    suspend fun isLoginTeacher(): TeacherAuthDetails
    suspend fun signOut(): Boolean
    suspend fun getAuthToken() : String?
    suspend fun getTeacherDetails() : UserInfo?
}