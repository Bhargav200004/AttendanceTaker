package com.example.attendancetaker.domain.teacher.repository

import com.example.attendancetaker.domain.teacher.model.Classroom

interface IClassRoom {

    suspend fun createClassRoom(classroom: Classroom)

}