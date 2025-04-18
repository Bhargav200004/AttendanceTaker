package com.example.attendancetaker.ui.teacher

import androidx.compose.ui.graphics.Color
import java.util.Date
import java.util.UUID

data class TeacherData(
    val teacherName : String = "",
    val teacherEmail : String = "",
    val classRoom : String = "",
    val assignedClassId : UUID? = null,
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
    val studentId : String = "",
    val classRoom : String = "",
    val past7DaysAttendance : List<Last7DaysAttendance> = emptyList()
)

data class Last7DaysAttendance(
    val date : Date,
    val attendance : AttendanceAndColor,
)

enum class AttendanceAndColor(val color : Color){
    HOLIDAY(color = Color.Gray),
    ABSENT(color = Color(210,104, 110) ),
    PRESENT(color = Color(146,210,147))
}


