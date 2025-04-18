package com.example.attendancetaker.ui.student

import androidx.compose.ui.graphics.Color
import java.time.LocalDate
import java.util.Date

data class StudentData(
    val studentId : String = "",
    val studentName : String = "",
    val studentRollNumber : String = "",
    val showStudentDialog : Boolean = false,
    val attendanceModel : List<AttendanceModel> = emptyList(),
    val attendanceMap : Map<LocalDate?, AttendanceAndColor> = emptyMap()
)

data class AttendanceModel(
    val date : Date,
    val attendance : AttendanceAndColor,
)

enum class AttendanceAndColor(val color : Color){
    HOLIDAY(color = Color.Gray),
    ABSENT(color = Color(210,104, 110) ),
    PRESENT(color = Color(146,210,147))
}