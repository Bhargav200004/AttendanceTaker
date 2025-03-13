package com.example.attendancetaker.repository

import com.example.attendancetaker.domain.teacher.dto.ClassroomDto
import com.example.attendancetaker.domain.teacher.model.Classroom
import com.example.attendancetaker.domain.teacher.repository.IClassRoom
import io.github.jan.supabase.postgrest.Postgrest
import timber.log.Timber
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

}