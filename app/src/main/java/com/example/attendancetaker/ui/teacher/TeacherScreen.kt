package com.example.attendancetaker.ui.teacher


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.attendancetaker.ui.teacher.components.AttendanceGrid
import com.example.attendancetaker.ui.teacher.components.CreateClassScreen
import com.example.attendancetaker.ui.teacher.components.DialogWithClassAndSectionInput
import com.example.attendancetaker.ui.teacher.components.MainTeacherScreenTopBar
import kotlinx.coroutines.launch

@Composable
fun TeacherScreen(modifier: Modifier = Modifier) {

    val viewModel: TeacherViewModel = hiltViewModel()

    val uiState by viewModel.state.collectAsStateWithLifecycle()

    MainTeacherScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )


//    if (uiState.isLoading){
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            CircularProgressIndicator()
//        }
//    }
//    else{
//        if (uiState.isClassRoomEmpty) {
//            CreateClassScreen(
//                modifier = modifier,
//                onCreateButtonClick = {
//                    viewModel.onEvent(TeacherEvent.OnShowClassRoomDialogChange(uiState.showClassRoomDialog))
//                }
//            )
//        }else {
//            MainTeacherScreen(
//                uiState = uiState,
//                onEvent = viewModel::onEvent
//            )
//        }
//    }

    if (uiState.showStudentDialog) {
        DialogWithStudentInput(
            uiState = uiState,
            onEvent = viewModel::onEvent
        )
    }

    if (uiState.showClassRoomDialog) {
        DialogWithClassAndSectionInput(
            uiState = uiState, onEvent = viewModel::onEvent
        )
    }
}


@Composable
fun MainTeacherScreen(
    uiState: TeacherData,
    onEvent: (TeacherEvent) -> Unit,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                SidePanelContent(
                    teacherName = uiState.teacherName,
                    teacherEmail = uiState.teacherEmail,
                    onAddStudentButtonClick = {
                        onEvent(TeacherEvent.OnShowStudentDialogChange(uiState.showStudentDialog))
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                MainTeacherScreenTopBar(
                    classRoom = uiState.classRoom,
                    onSidePanelClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    })
            },
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues = it)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(0) {
                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "GUJJLA BHARGAV",
                                style = MaterialTheme.typography.headlineLarge
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "33200122003",
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                Text(
                                    text = "10 'B'",
                                    style = MaterialTheme.typography.headlineSmall
                                )
                            }

                            AttendanceGrid()
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun SidePanelContent(
    teacherName: String,
    teacherEmail: String,
    onAddStudentButtonClick: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = teacherName,
            style = MaterialTheme.typography.displaySmall
        )
        Text(
            text = teacherEmail
        )
        OutlinedButton(
            shape = RoundedCornerShape(10.dp),
            onClick = onAddStudentButtonClick
        ) {
            Text(text = "Add Student")
        }
        OutlinedButton(
            shape = RoundedCornerShape(10.dp),
            onClick = {}
        ) {
            Text(text = "Sign Out")
        }
    }
}


@Composable
fun DialogWithStudentInput(
    uiState: TeacherData,
    onEvent: (TeacherEvent) -> Unit,
) {
    Dialog(
        onDismissRequest = {
            onEvent(TeacherEvent.OnShowStudentDialogChange(uiState.showStudentDialog))
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
                            onEvent(TeacherEvent.OnStudentNameChange(studentName = studentName))
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
                            onEvent(TeacherEvent.OnStudentRollNumberChange(studentRollNumber = studentRollNumber))
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
                                onEvent(TeacherEvent.OnShowStudentDialogChange(uiState.showStudentDialog))
                            },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Cancel"
                            )
                        }
                        OutlinedButton(
                            onClick = {
                                onEvent(TeacherEvent.OnStudentSubmitChange)
                                onEvent(TeacherEvent.OnShowStudentDialogChange(uiState.showStudentDialog))
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
