package com.example.attendancetaker.repository

import com.example.attendancetaker.domain.teacher.dto.StudentDto
import com.example.attendancetaker.domain.teacher.model.Student
import com.example.attendancetaker.domain.teacher.repository.IStudent
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import jakarta.inject.Inject
import timber.log.Timber

class StudentImpl @Inject constructor(
    private val database : Postgrest
) : IStudent {

    override suspend fun createStudent(student: Student) {
        try {
            val studentDto = StudentDto(
                studentId = student.studentId,
                studentName = student.studentName,
                studentRollNumber = student.studentRollNumber,
                classAssigned = student.classAssigned
            )
            database.from("students").insert(studentDto)
        }catch (e : PostgrestRestException){
            Timber.e("createStudent: $e")
        }
    }

}