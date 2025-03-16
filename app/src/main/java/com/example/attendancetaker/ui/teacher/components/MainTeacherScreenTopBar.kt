package com.example.attendancetaker.ui.teacher.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTeacherScreenTopBar(
    classRoom : String,
    onSidePanelClick : () -> Unit
) {
    TopAppBar(
        title = { Text(text = "Class $classRoom ")},
        navigationIcon = { IconButton(onClick = onSidePanelClick){
            Icon(imageVector = Icons.AutoMirrored.Filled.List, contentDescription = "Navigation List")
        } },
        windowInsets = WindowInsets.statusBars
    )
}