package com.example.attendancetaker.domain.teacher.model

import java.util.UUID

data class Student(
    val studentId : UUID,
    val studentName : String,
    val studentRollNumber : String,
    val classAssigned : UUID
)