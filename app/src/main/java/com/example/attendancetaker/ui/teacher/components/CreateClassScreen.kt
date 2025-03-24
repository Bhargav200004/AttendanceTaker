package com.example.attendancetaker.ui.teacher.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Class
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CreateClassScreen(
    modifier: Modifier,
    onCreateButtonClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            modifier = Modifier.size(200.dp),
            imageVector = Icons.Outlined.Class,
            contentDescription = "Class image"
        )

        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "You Don't have any class",
            style = TextStyle(
                fontSize = 18.sp
            )
        )
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Create a class",
            style = TextStyle(
                fontSize = 18.sp
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedButton(
            onClick = {
                onCreateButtonClick()
            },
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "Create a Class",
                style = TextStyle(
                    fontSize = 18.sp
                )
            )
        }
    }
}