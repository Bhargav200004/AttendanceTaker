package com.example.attendancetaker.ui.teacher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendancetaker.MySharedPreferenceDataStore
import com.example.attendancetaker.domain.teacher.model.Classroom
import com.example.attendancetaker.domain.teacher.model.Student
import com.example.attendancetaker.repository.AttendanceImpl
import com.example.attendancetaker.repository.AuthenticationImpl
import com.example.attendancetaker.repository.ClassRoomImpl
import com.example.attendancetaker.repository.StudentImpl
import com.example.attendancetaker.repository.TeacherImpl
import com.example.attendancetaker.utils.AttendanceType
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
import java.time.LocalDate
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TeacherViewModel @Inject constructor(
    private val classRoomImpl: ClassRoomImpl,
    private val teacherImpl : TeacherImpl,
    private val studentImpl : StudentImpl,
    private val attendanceImpl: AttendanceImpl,
    private val preferenceDataStore: MySharedPreferenceDataStore,
    private val authenticationImpl : AuthenticationImpl
) : ViewModel() {


    private val _state = MutableStateFlow(TeacherData())
    val state = _state.asSharedFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = TeacherData()
        )

    init {
        getInitialTeacherData()
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
                    _state.update { state ->
                        state.copy(
                            isLoading = false,
                            isClassRoomEmpty = true
                        )
                    }
                    return@launch
                }

                val classRoomDetail = classRoomImpl.getClassRoom(teacherDetail.assignedClass)

                val studentDetails = studentImpl.getAllStudentDetailWithClassRoomId(teacherDetail.assignedClass)

                val getPast7DaysAttendance = attendanceImpl.getAttendancePast7Days(teacherDetail.assignedClass)

                if (studentDetails != null) {
                    _state.update { state ->
                        state.copy(
                            studentData = studentDetails.map {
                                StudentData(
                                    studentName = it.studentName,
                                    studentRollNumber = it.studentRollNumber,
                                    classRoom = classRoomDetail?.className.toString() + " " + classRoomDetail?.classSection,
                                    studentId = it.studentId.toString(),
                                    past7DaysAttendance = getPast7DaysAttendance.mapNotNull { attendanceDto ->
                                        if (attendanceDto.studentId == it.studentId){
                                            Last7DaysAttendance(
                                                date = attendanceDto.attendanceDate ,
                                                attendance = when(attendanceDto.attendanceType){
                                                    AttendanceType.HOLIDAY -> AttendanceAndColor.HOLIDAY
                                                    AttendanceType.ABSENT -> AttendanceAndColor.ABSENT
                                                    AttendanceType.PRESENT -> AttendanceAndColor.PRESENT
                                                },
                                            )
                                        }else{
                                            null
                                        }
                                    }
                                )
                            }
                        )
                    }
                }

                _state.update {state ->
                    state.copy(
                        assignedClassId = teacherDetail.assignedClass,
                        teacherName = teacherDetail.teacherName,
                        teacherEmail = teacherEmail,
                        classRoom = classRoomDetail?.className.toString() + " " + classRoomDetail?.classSection,
                        isClassRoomEmpty = false
                    )
                }


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
            TeacherEvent.OnSignOutButtonClick -> onSignOutButtonClick()
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
                getInitialTeacherData()
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
                    studentRollNumber = state.value.studentRollNumber.toInt(),
                    classAssigned = state.value.assignedClassId!!
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
                getInitialTeacherData()
            }catch (e : Exception){
                Timber.e("onStudentSubmitChange: ${e.message}")
            }
        }
    }

    private fun onSignOutButtonClick() {
        viewModelScope.launch {
            try {
                preferenceDataStore.onClean()
                if(authenticationImpl.signOut()){
                    SnackBarController.sendEvent(
                        event = SnackBarEvent(
                            message = "Successfully Log Out"
                        )
                    )
                }
            }catch (e : Exception){
                Timber.e("onSignOutButtonClick: ${e.message}")
            }
        }
    }

}