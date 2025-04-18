package com.example.attendancetaker.ui.student

sealed class StudentEvent {

    data class AssignedStudentId(val studentId : String) : StudentEvent()

    data class OnStudentDialogShow(val show : Boolean) :  StudentEvent()

    data class OnStudentNameChange(val studentName : String) : StudentEvent()

    data class OnStudentRollNumberChange(val studentRollNumber : String) : StudentEvent()

    data object OnStudentSubmitChange : StudentEvent()
}