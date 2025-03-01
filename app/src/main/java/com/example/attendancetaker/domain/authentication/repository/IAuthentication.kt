package com.example.attendancetaker.domain.authentication.repository

interface IAuthentication {
    suspend fun signIn (email: String, password: String): Boolean
    suspend fun signUp (email: String, password: String): Boolean
}