package com.example.attendancetaker.domain.teacher.repository

import com.example.attendancetaker.domain.teacher.dto.ClassroomDto
import com.example.attendancetaker.domain.teacher.model.Classroom
import java.util.UUID

interface IClassRoom {

    suspend fun createClassRoom(classroom: Classroom)

    suspend fun getClassRoom(classRoomId : UUID) : ClassroomDto?

}