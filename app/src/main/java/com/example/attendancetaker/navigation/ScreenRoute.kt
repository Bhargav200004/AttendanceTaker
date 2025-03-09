package com.example.attendancetaker.navigation

import kotlinx.serialization.Serializable


@Serializable
sealed class AuthScreen{
    @Serializable
    data object Signin : AuthScreen()
    @Serializable
    data object Signup : AuthScreen()
}

@Serializable
sealed class TeacherScreen{
    @Serializable
    data object Teacher : TeacherScreen()
}