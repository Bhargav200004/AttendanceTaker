package com.example.attendancetaker.ui.attendance

data class AttendanceData(
    val assignedClassId: String = "",
    val attendanceData : List<AttendanceModel> = emptyList<AttendanceModel>()
)


data class AttendanceModel(
    val studentId : String,
    val studentName : String ,
    val studentRollNumber : Int,
    val isPresent : Boolean = false
)