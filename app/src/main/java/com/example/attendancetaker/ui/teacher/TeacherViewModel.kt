package com.example.attendancetaker.ui.teacher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendancetaker.MySharedPreferenceDataStore
import com.example.attendancetaker.domain.teacher.model.Classroom
import com.example.attendancetaker.domain.teacher.model.Student
import com.example.attendancetaker.repository.ClassRoomImpl
import com.example.attendancetaker.repository.StudentImpl
import com.example.attendancetaker.repository.TeacherImpl
import com.example.attendancetaker.utils.SnackBarController
import com.example.attendancetaker.utils.SnackBarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TeacherViewModel @Inject constructor(
    private val classRoomImpl: ClassRoomImpl,
    private val teacherImpl : TeacherImpl,
    private val studentImpl : StudentImpl,
    private val preferenceDataStore: MySharedPreferenceDataStore
) : ViewModel() {


    private val _state = MutableStateFlow(TeacherData())
    val state = _state.asSharedFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = TeacherData()
        )

    init {
//        getInitialTeacherData()
    }

    private fun getInitialTeacherData() {
        viewModelScope.launch {
            try {
                _state.update {state ->
                    state.copy(
                        isLoading = true
                    )
                }
                val teacherId = preferenceDataStore.getTeacherId()
                val teacherEmail = preferenceDataStore.getTeacherEmail()
                if (teacherId.isEmpty()) return@launch
                if (teacherEmail.isEmpty()) return@launch

                val teacherDetail = teacherImpl.getTeacherById(UUID.fromString(teacherId))
                if (teacherDetail?.assignedClass == null) {
                    return@launch
                }
                val classRoomDetail = classRoomImpl.getClassRoom(teacherDetail.assignedClass)
                
                Timber.d("getInitialTeacherData: $classRoomDetail")

                _state.update {state ->
                    state.copy(
                        teacherName = teacherDetail.teacherName,
                        teacherEmail = teacherEmail,
                        classRoom = classRoomDetail?.className.toString() + " " + classRoomDetail?.classSection,
                        isClassRoomEmpty = false
                    )
                }
                Timber.d("getInitialTeacherData: ${state.value} + $classRoomDetail")
                _state.update {state ->
                    state.copy(
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                Timber.e("getInitialTeacherData: ${e.message}")
            }
        }
    }

    fun onEvent(event : TeacherEvent){
        when(event){
            is TeacherEvent.OnClassRoomChange -> onClassRoomChange(classRoom = event.classroom)
            is TeacherEvent.OnSectionChange -> onSectionChange(section = event.section)
            is TeacherEvent.OnShowClassRoomDialogChange -> onShowClassRoomDialogChange(show = event.show)
            is TeacherEvent.OnShowStudentDialogChange -> onShowStudentDialogChange(show = event.show)
            is TeacherEvent.OnStudentNameChange -> onStudentNameChange(studentName = event.studentName)
            is TeacherEvent.OnStudentRollNumberChange -> onStudentRollNumberChange(studentRollNumber = event.studentRollNumber)
            TeacherEvent.OnClassRoomSubmitChange -> onClassRoomSubmitChange()
            TeacherEvent.OnStudentSubmitChange -> onStudentSubmitChange()
        }
    }


    private fun onStudentNameChange(studentName: String) {
        viewModelScope.launch {
            _state.update{state ->
                state.copy(
                    studentName = studentName
                )
            }
        }
    }

    private fun onStudentRollNumberChange(studentRollNumber: String) {
        viewModelScope.launch {
            _state.update {state ->
                state.copy(
                    studentRollNumber = studentRollNumber
                )
            }
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

    private fun onShowClassRoomDialogChange(show: Boolean) {
        viewModelScope.launch {
            _state.update {state ->
                state.copy(
                    showClassRoomDialog = !show
                )
            }
        }
    }

    private fun onShowStudentDialogChange(show: Boolean) {
        viewModelScope.launch {
            _state.update {state ->
                state.copy(
                    showStudentDialog = !show
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

    private fun onClassRoomSubmitChange() {
        viewModelScope.launch {
            try {

                val classroom = Classroom(
                    classId = UUID.randomUUID(),
                    className = state.value.classRoom.toInt(),
                    classSection = state.value.selectedSection
                )

                classRoomImpl.createClassRoom(
                    classroom = classroom
                )

                val teacherId = preferenceDataStore.getTeacherId()

                teacherImpl.updateTeacherAssignedClass(classroom.classId.toString() , teacherId )

                SnackBarController.sendEvent(
                    event = SnackBarEvent(
                        message = "Successfully Created Class"
                    )
                )
            }
            catch (e : Exception){
                Timber.e("onSubmitChange: ${e.message}")
            }
        }
    }

    private fun onStudentSubmitChange() {
        viewModelScope.launch {
            try {

                val student = Student(
                    studentId = UUID.randomUUID(),
                    studentName = state.value.studentName,
                    studentRollNumber = state.value.studentRollNumber,
                    classAssigned = state.value.assignedClassId
                )

                studentImpl.createStudent(student)

                _state.update {state ->
                    state.copy(
                        studentName = "",
                        studentRollNumber = ""
                    )
                }

                SnackBarController.sendEvent(
                    event = SnackBarEvent(
                        message = "Successfully Student Added"
                    )
                )
            }catch (e : Exception){
                Timber.e("onStudentSubmitChange: ${e.message}")
            }
        }
    }

}