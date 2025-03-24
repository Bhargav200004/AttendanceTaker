package com.example.attendancetaker.ui.attendance

sealed class AttendanceEvent {

    data class OnRadioButtonClick(val studentId : String , val isPresent : Boolean) : AttendanceEvent()

    data class AssignedClassId(val assignedClassId : String) : AttendanceEvent()

    data object OnSubmitButtonClick : AttendanceEvent()
}