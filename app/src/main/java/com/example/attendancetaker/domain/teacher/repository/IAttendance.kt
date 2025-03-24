package com.example.attendancetaker.domain.teacher.repository

import com.example.attendancetaker.domain.teacher.model.Attendance

interface IAttendance {

    suspend fun createAttendance(attendances: List<Attendance>)

}