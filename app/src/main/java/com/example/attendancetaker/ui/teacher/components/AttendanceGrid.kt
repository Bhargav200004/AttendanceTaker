package com.example.attendancetaker.ui.teacher.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.attendancetaker.ui.teacher.Last7DaysAttendance

@Composable
fun AttendanceGrid(
    past7DaysList: List<Last7DaysAttendance>
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {


        for (i in past7DaysList){
            AttendanceBox(
                backGroundColor = i.attendance.color,
                content = i.attendance.name.toString().firstChar()
            )
        }
    }
}

fun String.firstChar(): String {
    return if (this.isNotEmpty()) this[0].toString() else ""
}