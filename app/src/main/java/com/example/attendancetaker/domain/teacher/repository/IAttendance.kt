package com.example.attendancetaker.domain.teacher.repository

import com.example.attendancetaker.domain.teacher.dto.AttendanceDto
import com.example.attendancetaker.domain.teacher.model.Attendance
import java.util.UUID

interface IAttendance {

    suspend fun createAttendance(attendances: List<Attendance>)

    suspend fun getAttendancePast7Days(classId: UUID) :  List<AttendanceDto>

    suspend fun attendanceOnDateCheck(classId: UUID , attendanceDate : String): Boolean

    suspend fun getAttendanceById(studentId : UUID) : List<AttendanceDto>

}