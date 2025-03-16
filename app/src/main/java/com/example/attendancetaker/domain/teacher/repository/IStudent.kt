package com.example.attendancetaker.domain.teacher.repository

import com.example.attendancetaker.domain.teacher.model.Student

interface IStudent {

    suspend fun createStudent(student: Student)

}