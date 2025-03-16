package com.example.attendancetaker.repository


import com.example.attendancetaker.domain.teacher.dto.TeacherDto
import com.example.attendancetaker.domain.teacher.model.Teacher
import com.example.attendancetaker.domain.teacher.repository.ITeacher
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

class TeacherImpl @Inject constructor(
    private val database: Postgrest
) : ITeacher {

    override suspend fun createTeacher(teacher: Teacher) {
        try {
            val teacherDto = TeacherDto(
                teacherId = teacher.teacherId,
                assignedClass = null,
                teacherName = teacher.teacherName
            )
            database.from("teachers").insert(teacherDto)
        } catch (e: PostgrestRestException) {
            Timber.e("createTeacher: $e")
        }
    }

    override suspend fun updateTeacherAssignedClass(classRoomId: String, teacherId: String) {
        try {
            database.from("teachers").update(
                {
                    set("assigned_class", classRoomId)
                },
                {
                    filter {
                        eq("teacher_id", teacherId)
                    }
                }
            )
        } catch (e : Exception){
            Timber.e("updateTeacherAssignedClass: $e")
        }
    }

    override suspend fun getTeacherById(id: UUID) : TeacherDto?  {
        try {
            val result = database.from("teachers").select{
                filter {
                    eq("teacher_id",id)
                }
            }.decodeSingleOrNull<TeacherDto>()
            return result
        } catch (e : PostgrestRestException){
            return null
            Timber.e("getTeacherById: ${e.description}")
        }
    }

}