package com.example.attendancetaker.repository

import androidx.test.espresso.util.filter
import com.example.attendancetaker.domain.teacher.dto.StudentDto
import com.example.attendancetaker.domain.teacher.model.Student
import com.example.attendancetaker.domain.teacher.repository.IStudent
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import io.github.jan.supabase.postgrest.query.Order
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import timber.log.Timber
import java.util.UUID

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

    override suspend fun getAllStudentDetailWithClassRoomId(assignedClassId: UUID) : List<StudentDto>?{
         try {
            val result = database.from("students").select(){
                filter {
                    eq("class_assigned",assignedClassId)
                }
                order(
                    column = "student_rollnumber",
                    order = Order.ASCENDING
                )
            }.decodeList<StudentDto>()
            return result
        }catch (e : PostgrestRestException){
            Timber.e("getAllStudentDetailWithClassRoomId: $e")
             return null
        }
    }

}