package com.example.attendancetaker.ui.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendancetaker.repository.StudentImpl
import com.example.attendancetaker.utils.SnackBarController
import com.example.attendancetaker.utils.SnackBarEvent
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
import java.util.UUID


@HiltViewModel(assistedFactory = AttendanceViewModel.Factory::class)
class AttendanceViewModel @AssistedInject constructor(
    @Assisted val classId : String,
    private val studentImpl: StudentImpl
) : ViewModel() {

    @AssistedFactory interface Factory {
        fun create(classId : String) : AttendanceViewModel
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
                val result = studentImpl.getAllStudentDetailWithClassRoomId(UUID.fromString(classId))
                if (result == null) {
                    SnackBarController.sendEvent(
                        event = SnackBarEvent(
                            message = "No Student Data available"
                        )
                    )
                    return@launch
                }

                _state.update { state->
                    state.copy(
                        attendanceData = result.map {studentDto ->
                            AttendanceModel(
                                studentId = studentDto.studentId.toString() ,
                                studentName =  studentDto.studentName,
                                studentRollNumber = studentDto.studentRollNumber,
                            )
                        }
                    )
                }


            }catch (e : Exception){
                Timber.e("getStudentDetails: $e")
            }
        }
    }

    fun onEvent(event : AttendanceEvent){
        when(event){
            is AttendanceEvent.OnRadioButtonClick -> onRadioButtonClick(
                studentId = event.studentId,
                isPresent = event.isPresent
            )
            is AttendanceEvent.OnSubmitButtonClick -> onSubmitButtonClick()
            is AttendanceEvent.AssignedClassId -> assignedStudentId(assignedClassId = event.assignedClassId)
        }
    }

    private fun assignedStudentId(assignedClassId: String) {
        viewModelScope.launch {
            _state.update { state ->
                state.copy(
                    assignedClassId = assignedClassId
                )
            }
        }
    }

    private fun onSubmitButtonClick() {
        viewModelScope.launch {
         try {

         }catch (e : Exception){
                Timber.e("onSubmitButtonClick: $e")
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
                        }else{
                            it
                        }
                    }
                )
            }
        }
    }

}