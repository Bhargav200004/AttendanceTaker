package com.example.attendancetaker.ui.teacher.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun AttendanceGrid() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {

        val colorList = listOf<Color>(Color(210,104, 110) , Color.Gray , Color(255,255,197) , Color(146,210,147))
        val attendance = listOf<String>("P" , "A" , "H" )

        for (i in 1.. 7){
            AttendanceBox(
                backGroundColor = colorList.random(),
                content = attendance.random()
            )
        }
    }
}