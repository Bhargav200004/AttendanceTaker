package com.example.attendancetaker.domain.teacher.dto

import com.example.attendancetaker.utils.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ClassroomDto (

    @SerialName("class_id")
    @Serializable(with = UUIDSerializer::class)
    val classId : UUID,

    @SerialName("class_name")
    val className : Int,

    @SerialName("class_section")
    val classSection : String

    )