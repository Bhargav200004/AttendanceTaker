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
    val isClassRoomEmpty : Boolean = true,
    val isLoading : Boolean = false
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
