package com.example.attendancetaker.domain.teacher.model

import java.util.UUID

data class Classroom(
    val classId : UUID,
    val className : Int,
    val classSection : String
)
