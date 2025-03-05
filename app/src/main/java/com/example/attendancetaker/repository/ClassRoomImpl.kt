package com.example.attendancetaker.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.example.attendancetaker.domain.teacher.dto.ClassroomDto
import com.example.attendancetaker.domain.teacher.model.Classroom
import com.example.attendancetaker.domain.teacher.repository.IClassRoom
import io.github.jan.supabase.postgrest.Postgrest
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
            Log.e(TAG, "error => ${e.message}")
        }
    }

}