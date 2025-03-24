package com.example.attendancetaker.utils

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class AttendanceType{
    @SerialName("HOLIDAY")
    HOLIDAY,
    @SerialName("ABSENT")
    ABSENT ,
    @SerialName("PRESENT")
    PRESENT
}