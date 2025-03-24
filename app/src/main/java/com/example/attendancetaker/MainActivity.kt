package com.example.attendancetaker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.attendancetaker.navigation.AppNavigation
import com.example.attendancetaker.ui.attendance.AttendanceScreen
import com.example.attendancetaker.ui.theme.AttendanceTakerTheme
import com.example.attendancetaker.utils.ObserverAsEvent
import com.example.attendancetaker.utils.SnackBarController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AttendanceTakerTheme {

                val snackBarHostState = remember{
                    SnackbarHostState()
                }

                val scope = rememberCoroutineScope()


                ObserverAsEvent(flow = SnackBarController.event) {event ->
                    scope.launch {
                        snackBarHostState.currentSnackbarData?.dismiss()

                        val result = snackBarHostState.showSnackbar(
                            message = event.message,
                            actionLabel = event.actionLabel?.label,
                            duration = SnackbarDuration.Short
                        )

                        if (result == SnackbarResult.ActionPerformed) {
                            event.actionLabel?.action?.invoke()
                        }
                    }
                }


                Scaffold(
                    snackbarHost = {
                        SnackbarHost(hostState = snackBarHostState)
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    AppNavigation(
                        modifier = Modifier
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}