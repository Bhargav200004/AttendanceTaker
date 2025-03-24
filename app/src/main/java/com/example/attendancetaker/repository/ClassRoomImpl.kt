package com.example.attendancetaker.repository

import com.example.attendancetaker.domain.teacher.dto.ClassroomDto
import com.example.attendancetaker.domain.teacher.model.Classroom
import com.example.attendancetaker.domain.teacher.repository.IClassRoom
import io.github.jan.supabase.postgrest.Postgrest
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

class ClassRoomImpl @Inject constructor(
    private val database : Postgrest
): IClassRoom {
    override suspend fun createClassRoom(classroom: Classroom) {
        try {
            val classroomDto = ClassroomDto(
                classId = classroom.classId,
                className = classroom.className,
                classSection = classroom.classSection
            )
            database.from("classrooms").insert(classroomDto)
        }catch (e : Exception){
            Timber.e("createClassRoom: ${e.message}")
        }
    }


    override suspend fun getClassRoom(classRoomId: UUID) : ClassroomDto? {
        return try {
            val result = database.from("classrooms").select {
                filter {
                    eq("class_id" , classRoomId)
                }
            }.decodeSingleOrNull<ClassroomDto>()
            result
        }catch (e : Exception){
            Timber.e("getClassRoom: ${e.message}")
            null
        }
    }

}