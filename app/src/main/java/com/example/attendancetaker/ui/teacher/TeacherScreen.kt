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
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.attendancetaker.navigation.AttendanceScreen
import com.example.attendancetaker.navigation.AuthScreen
import com.example.attendancetaker.navigation.StudentScreen
import com.example.attendancetaker.ui.teacher.components.AttendanceGrid
import com.example.attendancetaker.ui.teacher.components.CreateClassScreen
import com.example.attendancetaker.ui.teacher.components.DialogWithClassAndSectionInput
import com.example.attendancetaker.ui.teacher.components.DialogWithStudentInput
import com.example.attendancetaker.ui.teacher.components.MainTeacherScreenTopBar
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun TeacherScreen(modifier : Modifier = Modifier, navController : NavController , classIdPassing : (String) -> Unit , studentIdPassing : (String) -> Unit) {

    val viewModel: TeacherViewModel = hiltViewModel()

    val uiState by viewModel.state.collectAsStateWithLifecycle()


    if (uiState.isLoading){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
    else{
        if (uiState.isClassRoomEmpty) {
            CreateClassScreen(
                modifier = modifier,
                onCreateButtonClick = {
                    viewModel.onEvent(TeacherEvent.OnShowClassRoomDialogChange(uiState.showClassRoomDialog))
                }
            )
        }else {
            MainTeacherScreen(
                uiState = uiState,
                onEvent = viewModel::onEvent,
                onAttendanceButtonClick = {
                    classIdPassing(uiState.assignedClassId.toString())
                    navController.navigate(route = AttendanceScreen.AttendanceNavigationRoute)
                },
                onStudentCardClick = {
                    studentIdPassing(it)
                    navController.navigate(route = StudentScreen.StudentNavigationRoute)
                },
                onSignOutButtonClick = {
                    navController.navigate(route = AuthScreen.SignupNavigationRoute)
                }
            )
        }
    }

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
    onAttendanceButtonClick : () -> Unit,
    onStudentCardClick : (studentId : String) -> Unit,
    onSignOutButtonClick: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                SidePanelContent(
                    teacherName = uiState.teacherName,
                    teacherEmail = uiState.teacherEmail,
                    onAttendanceButtonClick = onAttendanceButtonClick,
                    onAddStudentButtonClick = {
                        onEvent(TeacherEvent.OnShowStudentDialogChange(uiState.showStudentDialog))
                        scope.launch {
                            drawerState.close()
                        }

                    },
                    onSignOutButtonClick = {
                        onEvent(TeacherEvent.OnSignOutButtonClick)
                        scope.launch {
                            drawerState.close()
                        }
                        onSignOutButtonClick()
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
                items(uiState.studentData) {studentData ->
                    AttendanceCard(
                        name = studentData.studentName,
                        rollNumber = studentData.studentRollNumber,
                        classRoom = studentData.classRoom,
                        past7DaysList = studentData.past7DaysAttendance,
                        onStudentCardClick = {
                            onStudentCardClick(studentData.studentId)
                        }
                    )
                }

            }
        }
    }
}


@Composable
fun AttendanceCard(
    name : String,
    rollNumber : Int,
    classRoom : String,
    past7DaysList : List<Last7DaysAttendance>,
    onStudentCardClick : () -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onStudentCardClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.headlineLarge
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = rollNumber.toString(),
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = classRoom,
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            AttendanceGrid(
                past7DaysList = past7DaysList
            )
        }
    }
}


@Composable
fun SidePanelContent(
    teacherName: String,
    teacherEmail: String,
    onAttendanceButtonClick: ()  -> Unit,
    onAddStudentButtonClick: () -> Unit,
    onSignOutButtonClick : () -> Unit
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
            onClick = onAttendanceButtonClick
        ) {
            Text(text = "Add Attendance")
        }
        OutlinedButton(
            shape = RoundedCornerShape(10.dp),
            onClick = onAddStudentButtonClick
        ) {
            Text(text = "Add Student")
        }
        OutlinedButton(
            shape = RoundedCornerShape(10.dp),
            onClick = onSignOutButtonClick
        ) {
            Text(text = "Sign Out")
        }
    }
}