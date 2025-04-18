package com.example.attendancetaker.ui.teacher

sealed class TeacherEvent  {

    data class OnClassRoomChange(val classroom : String) : TeacherEvent()

    data class OnSectionChange(val section : String) : TeacherEvent()

    data class OnShowClassRoomDialogChange(val show : Boolean) : TeacherEvent()

    data class OnShowStudentDialogChange(val show : Boolean) : TeacherEvent()

    data class OnStudentNameChange(val studentName : String) : TeacherEvent()

    data class OnStudentRollNumberChange(val studentRollNumber : String) : TeacherEvent()

    data object OnSignOutButtonClick : TeacherEvent()

    data object OnStudentSubmitChange : TeacherEvent()

    data object OnClassRoomSubmitChange : TeacherEvent()
}