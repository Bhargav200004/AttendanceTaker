package com.example.attendancetaker.repository

import com.example.attendancetaker.domain.teacher.model.Attendance
import com.example.attendancetaker.domain.teacher.repository.IAttendance
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import timber.log.Timber
import javax.inject.Inject

class AttendanceImpl @Inject constructor(
    private val database : Postgrest
) : IAttendance {

    override suspend fun createAttendance(attendances: List<Attendance>) {
        try {
            database.from("attendance").insert(attendances)
        }catch (e : PostgrestRestException){
            Timber.e("createAttendance: $e")
        }
    }
}