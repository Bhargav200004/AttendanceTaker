package com.example.attendancetaker.domain.teacher.model

import com.example.attendancetaker.utils.AttendanceType
import java.util.Date
import java.util.UUID

data class Attendance (

    val attendanceId : UUID,

    val classId : UUID,

    val attendanceType : AttendanceType,

    val studentId : UUID,

    val attendanceDate : Date = Date()
)