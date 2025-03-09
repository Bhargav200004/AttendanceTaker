package com.example.attendancetaker.domain.teacher.model

import java.util.UUID

data class Teacher(
    val teacherId : UUID,
    val assignedClass : UUID?,
    val teacherName : String
)