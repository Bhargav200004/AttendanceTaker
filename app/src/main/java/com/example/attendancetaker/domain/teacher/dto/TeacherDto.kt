package com.example.attendancetaker.domain.teacher.dto

import com.example.attendancetaker.utils.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class TeacherDto(

    @SerialName("teacher_id")
    @Serializable(with = UUIDSerializer::class)
    val teacherId: UUID,

    @SerialName("assigned_class")
    @Serializable(with = UUIDSerializer::class)
    val assignedClass : UUID?,

    @SerialName("teacher_name")
    val teacherName : String

    )