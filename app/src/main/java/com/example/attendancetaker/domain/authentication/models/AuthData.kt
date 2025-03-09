package com.example.attendancetaker.domain.authentication.models

import io.github.jan.supabase.auth.exception.AuthRestException

data class AuthData(
    val successMessage : String? = null,
    val errorMessage : AuthRestException? = null
)