package com.example.attendancetaker.ui.teacher

sealed class TeacherEvent  {

    data class OnClassRoomChange(val classroom : String) : TeacherEvent()

    data class OnSectionChange(val section : String) : TeacherEvent()

    data class OnShowDialogChange(val show : Boolean) : TeacherEvent()

    data object OnSubmitChange : TeacherEvent()
}