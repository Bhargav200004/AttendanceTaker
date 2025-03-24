package com.example.attendancetaker.domain.teacher.model

import java.util.UUID

data class Student(
    val studentId : UUID,
    val studentName : String,
    val studentRollNumber : Int,
    val classAssigned : UUID
)