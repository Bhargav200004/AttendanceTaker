package com.example.attendancetaker.ui.student

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.attendancetaker.ui.student.components.EditStudentDetails
import com.example.attendancetaker.ui.student.components.StudentInformationCard
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun StudentScreen(navController: NavController, studentId: String) {

    val viewModel: StudentViewModel = hiltViewModel<StudentViewModel, StudentViewModel.Factory>(
        creationCallback = { factory ->
            factory.create(studentId = studentId)
        }
    )

    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        viewModel.onEvent(StudentEvent.AssignedStudentId(studentId = studentId))
    }


    Scaffold(
        topBar = {
            StudentScreenTopBar(
                studentName = uiState.studentName,
                navigationBackButton = {
                    navController.popBackStack()
                },
                onEditButtonClick = {
                    viewModel.onEvent(StudentEvent.OnStudentDialogShow(uiState.showStudentDialog))
                }
            )
        }
    ) { innerPaddingValue ->

        if (uiState.showStudentDialog) {
            EditStudentDetails(
                uiState = uiState,
                onEvent = viewModel::onEvent
            )
        }
        Column(
            modifier = Modifier
                .padding(paddingValues = innerPaddingValue)
        ) {
            StudentInformationCard(
                studentName = uiState.studentName,
                rollNumber = uiState.studentRollNumber
            )
            StudentAttendanceCalendar(
                uiState.attendanceMap
            )
        }
    }
}


@Composable
fun StudentAttendanceCalendar(attendanceMap: Map<LocalDate?, AttendanceAndColor>) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(24) }
    val endMonth = remember { currentMonth.plusMonths(24) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        val state = rememberCalendarState(
            startMonth = startMonth,
            endMonth = endMonth,
            firstVisibleMonth = currentMonth,
            firstDayOfWeek = firstDayOfWeekFromLocale(),
        )
        HorizontalCalendar(
            state = state,
            dayContent = { day ->
                Day(day, attendanceMap = attendanceMap)
            },
            monthHeader = { month -> MonthHeader(month) },
            calendarScrollPaged = false,
            contentPadding = PaddingValues(4.dp),
            monthContainer = { _, container ->
                val configuration = LocalConfiguration.current
                val screenWidth = configuration.screenWidthDp.dp
                Box(
                    modifier = Modifier
                        .width(screenWidth * 0.73f)
                        .padding(8.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                        .border(
                            color = Color.Blue,
                            width = 1.dp,
                            shape = RoundedCornerShape(8.dp),
                        ),
                ) {
                    container()
                }
            },
            monthBody = { _, content ->
                Box(
                    modifier = Modifier.background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.LightGray,
                                Color.White,
                            ),
                        ),
                    ),
                ) {
                    content()
                }
            },
        )
    }
}

@Composable
private fun MonthHeader(calendarMonth: CalendarMonth) {
    val daysOfWeek = calendarMonth.weekDays.first().map { it.date.dayOfWeek }
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(top = 6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = calendarMonth.yearMonth.displayText(short = true),
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            for (dayOfWeek in daysOfWeek) {
                Text(
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 15.sp,
                    text = dayOfWeek.displayText(uppercase = true, narrow = true),
                    fontWeight = FontWeight.Medium,
                )
            }
        }
        HorizontalDivider(color = Color.Black)
    }
}

fun YearMonth.displayText(short: Boolean = false): String {
    return "${this.month.displayText(short = short)} ${this.year}"
}

fun Month.displayText(short: Boolean = true): String {
    val style = if (short) TextStyle.SHORT else TextStyle.FULL
    return getDisplayName(style, Locale.ENGLISH)
}

fun DayOfWeek.displayText(uppercase: Boolean = false, narrow: Boolean = false): String {
    val style = if (narrow) TextStyle.NARROW else TextStyle.SHORT
    return getDisplayName(style, Locale.ENGLISH).let { value ->
        if (uppercase) value.uppercase(Locale.ENGLISH) else value
    }
}

@Composable
private fun Day(day: CalendarDay, attendanceMap: Map<LocalDate?, AttendanceAndColor>) {
    val dayDate = day.date
    val attendanceStatus = attendanceMap[dayDate]


    val color = when (attendanceStatus) {
        AttendanceAndColor.PRESENT -> attendanceStatus.color
        AttendanceAndColor.HOLIDAY -> attendanceStatus.color
        AttendanceAndColor.ABSENT -> attendanceStatus.color
        null ->  Color.Transparent
    }

    Box(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .size(40.dp)
            .clip(CircleShape)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        if (day.position == DayPosition.MonthDate) {
            Text(
                text = day.date.dayOfMonth.toString(),
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentScreenTopBar(
    studentName: String,
    onEditButtonClick: () -> Unit,
    navigationBackButton: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = studentName
            )
        },
        navigationIcon = {
            IconButton(
                onClick = navigationBackButton
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back Button for student Screen"
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    onEditButtonClick()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Button Student"
                )
            }
        }
    )
}