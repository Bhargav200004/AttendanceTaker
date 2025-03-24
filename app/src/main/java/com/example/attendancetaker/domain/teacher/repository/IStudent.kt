package com.example.attendancetaker.domain.teacher.repository

import com.example.attendancetaker.domain.teacher.dto.StudentDto
import com.example.attendancetaker.domain.teacher.model.Student
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface IStudent {

    suspend fun createStudent(student: Student)

    suspend fun getAllStudentDetailWithClassRoomId(assignedClassId : UUID) : List<StudentDto>?

}