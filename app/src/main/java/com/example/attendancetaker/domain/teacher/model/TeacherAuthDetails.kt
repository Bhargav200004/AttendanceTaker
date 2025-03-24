package com.example.attendancetaker.domain.teacher.model

data class TeacherAuthDetails(
    val userToken : String = "",
    val teacherId : String = "",
    val isLogin : Boolean = false
)