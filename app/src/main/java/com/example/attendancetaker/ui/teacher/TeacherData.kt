package com.example.attendancetaker.ui.teacher

data class TeacherData(
    val classRoom : String = "",
    val section : List<String> = getListOfSection() ,
    val selectedSection : String = "A",
    val showDialog : Boolean = false,
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
