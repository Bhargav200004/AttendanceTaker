package com.example.attendancetaker.ui.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendancetaker.repository.AttendanceImpl
import com.example.attendancetaker.repository.StudentImpl
import com.example.attendancetaker.utils.SnackBarController
import com.example.attendancetaker.utils.SnackBarEvent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.ZoneId
import java.util.UUID

@HiltViewModel(assistedFactory = StudentViewModel.Factory::class)
class StudentViewModel @AssistedInject constructor(
    @Assisted val studentId: String,
    private val studentImpl: StudentImpl,
    private val attendanceImpl: AttendanceImpl
) : ViewModel() {


    @AssistedFactory
    interface Factory {
        fun create(studentId: String): StudentViewModel
    }

    init {
        getStudentDetails()
    }

    private fun getStudentDetails() {
        viewModelScope.launch {
            try {
                val studentDetails = studentImpl.getStudentDetailsById(UUID.fromString(studentId))
                if (studentDetails == null) return@launch
                _state.update { state ->
                    state.copy(
                        studentName = studentDetails.studentName,
                        studentRollNumber = studentDetails.studentRollNumber.toString()
                    )
                }

                val attendanceDetails = attendanceImpl.getAttendanceById(UUID.fromString(studentId))

                val attendanceModels = attendanceDetails.map {
                    AttendanceModel(
                        date = it.attendanceDate,
                        attendance = AttendanceAndColor.entries.first { attendance ->
                            attendance.name == it.attendanceType.name
                        }
                    )
                }

                val attendanceMap = attendanceModels.associate { model ->
                    model.date.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate() to model.attendance
                }


                _state.update { state ->
                    state.copy(
                        attendanceModel = attendanceModels,
                        attendanceMap = attendanceMap
                    )
                }
            } catch (e: Exception) {
                Timber.e("getStudentDetails: $e")
            }
        }
    }


    private val _state = MutableStateFlow(StudentData())
    val state = _state.asSharedFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = StudentData()
        )

    fun onEvent(event: StudentEvent) {
        when (event) {
            is StudentEvent.AssignedStudentId -> assignedStudentId(assignedStudentId = event.studentId)
            is StudentEvent.OnStudentDialogShow -> onStudentDialogShow(show = event.show)
            is StudentEvent.OnStudentNameChange -> onStudentNameChange(studentName = event.studentName)
            is StudentEvent.OnStudentRollNumberChange -> onStudentRollNumberChange(studentRollNumber = event.studentRollNumber)
            StudentEvent.OnStudentSubmitChange -> onStudentSubmitChange()
        }

    }

    private fun onStudentSubmitChange() {
        viewModelScope.launch {
            try {
                studentImpl.updateStudentDetailsById(
                    studentId = state.value.studentId,
                    studentName = state.value.studentName,
                    studentRollNumber = state.value.studentRollNumber.toInt()
                )
                SnackBarController.sendEvent(
                    event = SnackBarEvent(
                        message = "Successfully Updated Student Details"
                    )
                )
            } catch (e: Exception) {
                Timber.e("onStudentSubmitChange: $e")
            }
        }
    }

    private fun onStudentRollNumberChange(studentRollNumber: String) {
        viewModelScope.launch {
            _state.update { state ->
                state.copy(
                    studentRollNumber = studentRollNumber
                )
            }
        }
    }

    private fun onStudentNameChange(studentName: String) {
        viewModelScope.launch {
            _state.update { state ->
                state.copy(
                    studentName = studentName
                )
            }
        }
    }

    private fun onStudentDialogShow(show: Boolean) {
        viewModelScope.launch {
            _state.update { state ->
                state.copy(
                    showStudentDialog = !show
                )
            }
        }
    }

    private fun assignedStudentId(assignedStudentId: String) {
        viewModelScope.launch {
            _state.update { state ->
                state.copy(
                    studentId = assignedStudentId
                )
            }
        }
    }

}