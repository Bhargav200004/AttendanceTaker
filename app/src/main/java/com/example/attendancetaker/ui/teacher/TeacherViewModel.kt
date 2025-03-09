package com.example.attendancetaker.ui.teacher

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendancetaker.MySharedPreferenceDataStore
import com.example.attendancetaker.domain.teacher.model.Classroom
import com.example.attendancetaker.repository.ClassRoomImpl
import com.example.attendancetaker.utils.SnackBarController
import com.example.attendancetaker.utils.SnackBarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TeacherViewModel @Inject constructor(
    private val classRoomImpl: ClassRoomImpl,
    private val preferenceDataStore: MySharedPreferenceDataStore
) : ViewModel() {


    private val _state = MutableStateFlow(TeacherData())
    val state = _state.asSharedFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = TeacherData()
        )

    fun onEvent(event : TeacherEvent){
        when(event){
            is TeacherEvent.OnClassRoomChange -> onClassRoomChange(classRoom = event.classroom)
            is TeacherEvent.OnSectionChange -> onSectionChange(section = event.section)
            is TeacherEvent.OnShowDialogChange -> onShowDialogChange(show = event.show)
            TeacherEvent.OnSubmitChange -> onSubmitChange()
        }
    }

    private fun onSectionChange(section: String) {
        viewModelScope.launch {
            _state.update {state ->
                state.copy(
                    selectedSection = section
                )
            }
        }
    }

    private fun onSubmitChange() {
        viewModelScope.launch {
            try {

                val classroom = Classroom(
                    classId = UUID.randomUUID(),
                    className = state.value.classRoom,
                    classSection = state.value.selectedSection
                )

                val teacherId = preferenceDataStore.getTeacherId()

//                classRoomImpl.createClassRoom(
//                    classroom = classroom
//                )

                SnackBarController.sendEvent(
                    event = SnackBarEvent(
                        message = "Successfully Created Class"
                    )
                )
            }
            catch (e : Exception){
                Log.e(TAG , "error => ${e.message}")
            }
        }
    }

    private fun onShowDialogChange(show: Boolean) {
        viewModelScope.launch {
            _state.update {state ->
                state.copy(
                    showDialog = !show
                )
            }
        }
    }

    private fun onClassRoomChange(classRoom: String) {
        viewModelScope.launch {
            _state.update {state ->
                state.copy(
                    classRoom = classRoom
                )
            }
        }
    }

}