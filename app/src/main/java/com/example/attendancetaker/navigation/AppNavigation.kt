package com.example.attendancetaker.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.attendancetaker.notification.HolidayList.holidays
import com.example.attendancetaker.notification.scheduleNextValidDay
import com.example.attendancetaker.ui.attendance.AttendanceScreen
import com.example.attendancetaker.ui.authentication.signIn.SignInScreen
import com.example.attendancetaker.ui.authentication.signUp.SignUpScreen
import com.example.attendancetaker.ui.student.StudentScreen
import com.example.attendancetaker.ui.teacher.TeacherScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier , context: Context ) {

    val navHostController = rememberNavController()

    val viewModel: IsLoginTeacherViewModel = hiltViewModel()

    val authenticationType by viewModel.state.collectAsStateWithLifecycle()

    NavHost(navController = navHostController , startDestination = if(authenticationType) {
        scheduleNextValidDay(context = context , holidays = holidays)
        TeacherScreen.TeacherNavigationRoute
    } else AuthScreen.SignupNavigationRoute){
        composable<AuthScreen.SignupNavigationRoute>{
            SignUpScreen(navController = navHostController)
        }
        composable<AuthScreen.SigninNavigationRoute>{
                SignInScreen(navController = navHostController)
        }
        composable<TeacherScreen.TeacherNavigationRoute> {
            TeacherScreen(
                navController = navHostController,
                studentIdPassing = {studentId ->
                    navHostController.currentBackStackEntry?.savedStateHandle?.set("studentId" , studentId)
                },
                classIdPassing = {assignedClassId ->
                    navHostController.currentBackStackEntry?.savedStateHandle?.set("assignedClassId",assignedClassId)
                }
            )
        }
        composable<AttendanceScreen.AttendanceNavigationRoute> {
            val assignedClassId = navHostController.previousBackStackEntry?.savedStateHandle?.get<String>("assignedClassId")
            AttendanceScreen(navController = navHostController , assignedClassId = assignedClassId.toString())
        }
        composable<StudentScreen.StudentNavigationRoute> {
            val studentId = navHostController.previousBackStackEntry?.savedStateHandle?.get<String>("studentId")
            StudentScreen(navController = navHostController , studentId = studentId.toString())
        }
    }
}