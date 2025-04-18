package com.example.attendancetaker.ui.attendance

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.attendancetaker.navigation.TeacherScreen
import com.example.attendancetaker.utils.functions.DateConverter.toDateProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(navController: NavController, assignedClassId: String) {

    val viewModel: AttendanceViewModel =
        hiltViewModel<AttendanceViewModel, AttendanceViewModel.Factory>(
            creationCallback = { factory ->
                factory.create(classId = assignedClassId)
            }
        )



    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        viewModel.onEvent(AttendanceEvent.AssignedClassId(assignedClassId = assignedClassId))
    }

    Scaffold(
        topBar = {
            AttendanceScreenTopBar(
                onBackButtonClick = {
                    navController.navigate(route = TeacherScreen.TeacherNavigationRoute)
                },
                onHolidayButtonClick = {
                    viewModel.onEvent(AttendanceEvent.OnHolidayButtonClick)
                },
                onDoneButtonClick = {
                    viewModel.onEvent(AttendanceEvent.OnSubmitButtonClick)
                }
            )
        }
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValue)
        ) {
            HeaderInfoSection()
            LazyColumn {

                items(uiState.attendanceData) { attendanceData ->
                    AttendanceTackerContainer(
                        studentId = attendanceData.studentId,
                        rollNumber = attendanceData.studentRollNumber,
                        studentName = attendanceData.studentName,
                        isPresent = attendanceData.isPresent,
                        onRadioButtonClick = { studentId, isPresent ->
                            viewModel.onEvent(
                                AttendanceEvent.OnRadioButtonClick(
                                    studentId = studentId,
                                    isPresent = isPresent
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun AttendanceTackerContainer(
    studentId: String,
    rollNumber: Int,
    studentName: String,
    isPresent: Boolean,
    onRadioButtonClick: (studentId: String, isPresent: Boolean) -> Unit
) {
    HorizontalDivider()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .weight(0.7f),
            textAlign = TextAlign.Center,
            text = rollNumber.toString()
        )
        VerticalDivider()
        Text(
            modifier = Modifier
                .weight(1f),
            textAlign = TextAlign.Center,
            text = studentName
        )
        VerticalDivider()
        RadioButton(
            modifier = Modifier
                .weight(0.8f),
            selected = isPresent,
            onClick = {
                onRadioButtonClick(studentId, isPresent)
            }
        )

    }
    HorizontalDivider()
}

@Composable
private fun HeaderInfoSection() {
    HorizontalDivider()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .weight(0.7f),
            textAlign = TextAlign.Center,
            text = "Roll no"
        )
        VerticalDivider()
        Text(
            modifier = Modifier
                .weight(1f),
            textAlign = TextAlign.Center,
            text = "Name"
        )
        VerticalDivider()
        Text(
            modifier = Modifier
                .weight(0.8f),
            textAlign = TextAlign.Center,
            text = "Present/Absent"
        )
    }
    HorizontalDivider()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreenTopBar(
    onBackButtonClick: () -> Unit,
    onHolidayButtonClick: () -> Unit,
    onDoneButtonClick: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text("Attendance")
                Text(
                    text = toDateProvider(),
                    style = MaterialTheme.typography.titleMedium
                )
            }

        },
        navigationIcon = {
            IconButton(onClick = onBackButtonClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back Button"
                )
            }
        },
        actions = {
            Button(
                modifier = Modifier.padding(start = 10.dp),
                onClick = onHolidayButtonClick,
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "On Holiday")
            }
            Button(
                modifier = Modifier.padding(start = 10.dp),
                onClick = onDoneButtonClick,
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "Done")
            }
        }
    )
}