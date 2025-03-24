package com.example.attendancetaker.domain.teacher.dto

import com.example.attendancetaker.utils.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class StudentDto(

    @SerialName("student_id")
    @Serializable(with = UUIDSerializer::class)
    val studentId : UUID,

    @SerialName("student_name")
    val studentName : String,

    @SerialName("student_rollnumber")
    val studentRollNumber : Int,

    @SerialName("class_assigned")
    @Serializable(with = UUIDSerializer::class)
    val classAssigned : UUID

)