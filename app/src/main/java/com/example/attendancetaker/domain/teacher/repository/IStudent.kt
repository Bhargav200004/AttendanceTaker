package com.example.attendancetaker.domain.teacher.repository

import com.example.attendancetaker.domain.teacher.dto.StudentDto
import com.example.attendancetaker.domain.teacher.model.Student
import java.util.UUID

interface IStudent {

    suspend fun createStudent(student: Student)

    suspend fun updateStudentDetailsById(studentId : String, studentName : String , studentRollNumber : Int)

    suspend fun getStudentDetailsById(studentId : UUID) : StudentDto?

    suspend fun getAllStudentDetailWithClassRoomId(assignedClassId : UUID) : List<StudentDto>?

}