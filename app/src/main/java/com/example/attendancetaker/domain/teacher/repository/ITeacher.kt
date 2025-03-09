package com.example.attendancetaker.domain.teacher.repository

import com.example.attendancetaker.domain.teacher.dto.TeacherDto
import com.example.attendancetaker.domain.teacher.model.Teacher
import java.util.UUID

interface ITeacher {

    suspend fun createTeacher(teacher: Teacher)

    suspend fun updateTeacher(teacher: Teacher)

    suspend fun getTeacherById(id: UUID) : TeacherDto?

}