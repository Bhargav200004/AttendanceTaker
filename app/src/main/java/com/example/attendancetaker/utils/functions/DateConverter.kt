package com.example.attendancetaker.utils.functions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateConverter {

    fun toDateProvider() : String {
        val sdf = SimpleDateFormat("dd/MM/yyy" , Locale.getDefault())
        val currentDate = sdf.format(Date())
        return currentDate
    }

}