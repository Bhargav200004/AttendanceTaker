package com.example.attendancetaker.navigation

import kotlinx.serialization.Serializable


@Serializable
sealed class AuthScreen{
    @Serializable
    data object SigninNavigationRoute : AuthScreen()
    @Serializable
    data object SignupNavigationRoute : AuthScreen()
}

@Serializable
sealed class TeacherScreen{
    @Serializable
    data object TeacherNavigationRoute : TeacherScreen()
}

@Serializable
sealed class AttendanceScreen{
    @Serializable
    data object AttendanceNavigationRoute : AttendanceScreen()
}

@Serializable
sealed class StudentScreen{
    @Serializable
    data object StudentNavigationRoute : StudentScreen()
}