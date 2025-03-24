package com.example.attendancetaker.ui.teacher

import java.util.UUID

data class TeacherData(
    val teacherName : String = "",
    val teacherEmail : String = "",
    val classRoom : String = "",
    val assignedClassId : UUID = UUID.fromString("66cc9dce-74b1-401f-b09d-69a917513e4d"),
    val section : List<String> = getListOfSection(),
    val selectedSection : String = "A",
    val showClassRoomDialog : Boolean = false,
    val showStudentDialog: Boolean = false,
    val studentName : String = "",
    val studentRollNumber : String = "",
    val isClassRoomEmpty : Boolean = false, // IT should be true for testng purpose
    val isLoading : Boolean = false,
    val studentData : List<StudentData> = emptyList<StudentData>()
){
    companion object{
        enum class Section(val value: String) {
            A(value = "A"),
            B(value = "B"),
            C(value = "C"),
        }

        fun getListOfSection() : List<String>{
            return listOf(
                "A",
                "B",
                "C",
            )
        }

    }
}

data class StudentData(
    val studentName : String = "",
    val studentRollNumber : Int = 0,
    val classRoom : String = "",
)


