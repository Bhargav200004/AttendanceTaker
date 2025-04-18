package com.example.attendancetaker.repository

import com.example.attendancetaker.domain.teacher.dto.AttendanceDto
import com.example.attendancetaker.domain.teacher.model.Attendance
import com.example.attendancetaker.domain.teacher.repository.IAttendance
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject

class AttendanceImpl @Inject constructor(
    private val database : Postgrest
) : IAttendance {

    override suspend fun createAttendance(attendances: List<Attendance>) {
        try {
            val attendanceDto = attendances.map {
                AttendanceDto(
                    attendanceId = it.attendanceId,
                    classId = it.classId,
                    attendanceType = it.attendanceType,
                    studentId = it.studentId,
                    attendanceDate = it.attendanceDate
                )
            }
            database.from("attendance").insert(attendanceDto)
        }catch (e : PostgrestRestException){
            Timber.e("createAttendance: $e")
        }
    }

    // Getting all attendance of a past 7 days
    override suspend fun getAttendancePast7Days(classId: UUID): List<AttendanceDto> {
        return try {
            val endDate = LocalDate.now()
            val startDate = endDate.minusDays(6)

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val startDateTime = startDate.atStartOfDay().format(formatter)       // 00:00:00
            val endDateTime = endDate.atTime(23, 59, 59).format(formatter)       // 23:59:59

            val result = database.from("attendance").select {
                filter {
                    eq("class_id", classId)
                    gte("attendance_date", startDateTime)
                    lte("attendance_date", endDateTime)
                }
            }.decodeList<AttendanceDto>()

            result
        } catch (e: PostgrestRestException) {
            Timber.e("getAttendancePast7Days: $e")
            emptyList()
        }
    }

    // Checking if attendance is already present on that date
    override suspend fun attendanceOnDateCheck(classId: UUID , attendanceDate : String): Boolean  {
        try {
             val response = database.from("attendance").select{
                filter {
                    eq("class_id" , classId)
                    gte("attendance_date", "$attendanceDate 00:00:00")
                    lt("attendance_date", "$attendanceDate 23:59:59")
                }
                limit(1)
            }
            val result =  response.decodeSingleOrNull<AttendanceDto>()

            return result == null
        }catch (e  : PostgrestRestException){
            Timber.e("getAttendanceOnDateCheck: $e")
            return false
        }
    }


    // getting Attendance by student id
    override suspend fun getAttendanceById(studentId : UUID): List<AttendanceDto> {
        try {
            val result = database.from("attendance").select{
                filter {
                    eq("student_id",studentId)
                }
            }.decodeList<AttendanceDto>()
            return result;
        }catch (e : PostgrestRestException){
            Timber.e("getAttendanceById: $e")
            throw e
        }
    }

}