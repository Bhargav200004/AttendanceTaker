package com.example.attendancetaker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.attendancetaker.navigation.AttendanceScreen
import com.example.attendancetaker.ui.attendance.AttendanceScreen
import com.example.attendancetaker.ui.authentication.signIn.SignInScreen
import com.example.attendancetaker.ui.authentication.signUp.SignUpScreen
import com.example.attendancetaker.ui.teacher.TeacherScreen

@Composable
fun AppNavigation(modifier: Modifier ) {

    val navHostController = rememberNavController()

    val viewModel: IsLoginTeacherViewModel = hiltViewModel()

    val authenticationType by viewModel.state.collectAsStateWithLifecycle()

    NavHost(navController = navHostController , startDestination = if(authenticationType)TeacherScreen.Teacher else AuthScreen.Signup){
        composable<AuthScreen.Signup>{
            SignUpScreen(navController = navHostController)
        }
        composable<AuthScreen.Signin>{
                SignInScreen(navController = navHostController)
        }
        composable<TeacherScreen.Teacher> {
            TeacherScreen(navController = navHostController){assignedClassId ->
                navHostController.currentBackStackEntry?.savedStateHandle?.set("assignedClassId",assignedClassId)
            }
        }
        composable<AttendanceScreen.Attendance> {
            val assignedClassId = navHostController.previousBackStackEntry?.savedStateHandle?.get<String>("assignedClassId")
            AttendanceScreen(navController = navHostController , assignedClassId = assignedClassId.toString())
        }
    }
}