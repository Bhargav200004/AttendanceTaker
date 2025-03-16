package com.example.attendancetaker.ui.teacher.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AttendanceBox(
    backGroundColor : Color = Color.White,
    content : String = ""
) {
    Box(
        modifier = Modifier
            .size(35.dp)
            .background(color = backGroundColor)
            .shadow(elevation = 1.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            style = MaterialTheme.typography.headlineSmall,
            text = content
        )
    }
}