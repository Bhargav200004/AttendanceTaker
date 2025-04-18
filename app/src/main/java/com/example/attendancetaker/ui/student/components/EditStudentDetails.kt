package com.example.attendancetaker.ui.student.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.attendancetaker.ui.student.StudentData
import com.example.attendancetaker.ui.student.StudentEvent

@Composable
fun EditStudentDetails(
    uiState : StudentData,
    onEvent: (StudentEvent) -> Unit,
) {
    Dialog(
        onDismissRequest = {
            onEvent(StudentEvent.OnStudentDialogShow(uiState.showStudentDialog))
        }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(14.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Enter Student Details",
                    modifier = Modifier.padding(16.dp),
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = uiState.studentName,
                        onValueChange = { studentName ->
                            onEvent(StudentEvent.OnStudentNameChange(studentName = studentName))
                        },
                        label = {
                            Text(
                                text = "Student Name"
                            )
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = uiState.studentRollNumber,
                        onValueChange = { studentRollNumber ->
                            onEvent(StudentEvent.OnStudentRollNumberChange(studentRollNumber = studentRollNumber))
                        },
                        label = {
                            Text(
                                text = "Student Roll No"
                            )
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = {
                                onEvent(StudentEvent.OnStudentDialogShow(uiState.showStudentDialog))
                            },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Cancel"
                            )
                        }
                        OutlinedButton(
                            onClick = {
                                onEvent(StudentEvent.OnStudentSubmitChange)
                                onEvent(StudentEvent.OnStudentDialogShow(uiState.showStudentDialog))
                            },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Submit"
                            )
                        }
                    }
                }
            }
        }
    }
}