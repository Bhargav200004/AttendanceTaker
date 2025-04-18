package com.example.attendancetaker.domain.teacher.dto

import com.example.attendancetaker.utils.AttendanceType
import com.example.attendancetaker.utils.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date
import java.util.UUID

@Serializable
data class AttendanceDto(

    @SerialName("attendance_id")
    @Serializable(with = UUIDSerializer::class)
    val attendanceId : UUID,

    @SerialName("class_id")
    @Serializable(with = UUIDSerializer::class)
    val classId : UUID,

    @SerialName("attendance_type")
    val attendanceType : AttendanceType,

    @SerialName("student_id")
    @Serializable(with = UUIDSerializer::class)
    val studentId : UUID,

    @SerialName("attendance_date")
    @Serializable(with = DateSerializer::class)
    val attendanceDate : Date = Date()

)

