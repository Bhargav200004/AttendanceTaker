package com.example.attendancetaker.ui.attendance


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendancetaker.domain.teacher.model.Attendance
import com.example.attendancetaker.repository.AttendanceImpl
import com.example.attendancetaker.repository.StudentImpl
import com.example.attendancetaker.utils.AttendanceType
import com.example.attendancetaker.utils.SnackBarController
import com.example.attendancetaker.utils.SnackBarEvent
import com.example.attendancetaker.utils.functions.DateConverter.toDateProvider
import com.example.attendancetaker.utils.functions.DateConverter.toFormattedDateOnly
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Date
import java.util.UUID


@HiltViewModel(assistedFactory = AttendanceViewModel.Factory::class)
class AttendanceViewModel @AssistedInject constructor(
    @Assisted val classId: String,
    private val studentImpl: StudentImpl,
    private val attendanceImpl: AttendanceImpl,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(classId: String): AttendanceViewModel
    }

    private val _state = MutableStateFlow(AttendanceData())
    val state = _state.asSharedFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = AttendanceData()
        )

    init {
        getStudentDetails()
    }

    private fun getStudentDetails() {
        viewModelScope.launch {
            try {
                val result =
                    studentImpl.getAllStudentDetailWithClassRoomId(UUID.fromString(classId))
                if (result == null) {
                    SnackBarController.sendEvent(
                        event = SnackBarEvent(
                            message = "No Student Data available"
                        )
                    )
                    return@launch
                }

                _state.update { state ->
                    state.copy(
                        attendanceData = result.map { studentDto ->
                            AttendanceModel(
                                studentId = studentDto.studentId.toString(),
                                studentName = studentDto.studentName,
                                studentRollNumber = studentDto.studentRollNumber,
                            )
                        }
                    )
                }


            } catch (e: Exception) {
                Timber.e("getStudentDetails: $e")
            }
        }
    }

    fun onEvent(event: AttendanceEvent) {
        when (event) {
            is AttendanceEvent.OnRadioButtonClick -> onRadioButtonClick(
                studentId = event.studentId,
                isPresent = event.isPresent
            )

            is AttendanceEvent.AssignedClassId -> assignedClassId(assignedClassId = event.assignedClassId)
            is AttendanceEvent.OnHolidayButtonClick -> onHolidayButtonClick()
            is AttendanceEvent.OnSubmitButtonClick -> onSubmitButtonClick()
        }
    }

    private fun assignedClassId(assignedClassId: String) {
        viewModelScope.launch {
            _state.update { state ->
                state.copy(
                    assignedClassId = assignedClassId
                )
            }
        }
    }

    private fun onRadioButtonClick(studentId: String, isPresent: Boolean) {
        viewModelScope.launch {
            _state.update { state ->
                state.copy(
                    attendanceData = state.attendanceData.map {
                        if (it.studentId == studentId) {
                            it.copy(
                                isPresent = !isPresent
                            )
                        } else {
                            it
                        }
                    }
                )
            }
        }
    }

    private fun onHolidayButtonClick() {
        viewModelScope.launch {
            try {

                val isDateAvailable = attendanceImpl.attendanceOnDateCheck(
                    UUID.fromString(classId),
                    attendanceDate = Date().toFormattedDateOnly()
                )

                if (!isDateAvailable) { // Date is Not available so it send the attendance already present on that day
                    SnackBarController.sendEvent(
                        event = SnackBarEvent(
                            message = "On this ${toDateProvider()} Holiday already given"
                        )
                    )
                    return@launch
                }


                val listOfAttendance: List<Attendance> =
                    state.value.attendanceData.map { attendanceModel ->
                        Attendance(
                            attendanceId = UUID.randomUUID(),
                            classId = UUID.fromString(classId),
                            attendanceType = AttendanceType.HOLIDAY,
                            studentId = UUID.fromString(attendanceModel.studentId),
                            attendanceDate = Date()
                        )
                    }

                attendanceImpl.createAttendance(listOfAttendance)

                SnackBarController.sendEvent(
                    event = SnackBarEvent(
                        message = "Successfully added Holiday on date ${toDateProvider()}"
                    )
                )

            } catch (e: Exception) {
                Timber.e("onHolidayButtonClick: $e")
            }
        }
    }

    private fun onSubmitButtonClick() {
        viewModelScope.launch {
            try {

                val isDateAvailable = attendanceImpl.attendanceOnDateCheck(
                    UUID.fromString(classId),
                    attendanceDate = Date().toFormattedDateOnly()
                )

                if (!isDateAvailable) { // Date is Not available so it send the attendance already present on that day
                    SnackBarController.sendEvent(
                        event = SnackBarEvent(
                            message = "On this ${toDateProvider()} attendance already given"
                        )
                    )
                    return@launch
                }


                val listOfAttendance: List<Attendance> =
                    state.value.attendanceData.map { attendanceModel ->
                        Attendance(
                            attendanceId = UUID.randomUUID(),
                            classId = UUID.fromString(classId),
                            attendanceType = if (attendanceModel.isPresent == true) AttendanceType.PRESENT else AttendanceType.ABSENT,
                            studentId = UUID.fromString(attendanceModel.studentId),
                            attendanceDate = Date()
                        )
                    }

                attendanceImpl.createAttendance(listOfAttendance)

                SnackBarController.sendEvent(
                    event = SnackBarEvent(
                        message = "Successfully added attendance on date ${toDateProvider()}"
                    )
                )

            } catch (e: Exception) {
                Timber.e("onSubmitButtonClick: $e")
            }
        }
    }
}