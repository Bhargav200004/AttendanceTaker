package com.example.attendancetaker.navigation

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendancetaker.MySharedPreferenceDataStore
import com.example.attendancetaker.repository.AuthenticationImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IsLoginTeacherViewModel @Inject constructor(
    private val isLoginTeacher : AuthenticationImpl,
    private val sharedPreferenceDataStore: MySharedPreferenceDataStore
): ViewModel() {

    private var _state = MutableStateFlow(false);
    val state = _state.asStateFlow().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = false
    )

    init {
        getAuthenticated()
    }

    private fun getAuthenticated(){
        viewModelScope.launch {
            try {
                val isLoginTeacher = isLoginTeacher.isLoginTeacher()
                if (isLoginTeacher.isLogin){
                    sharedPreferenceDataStore.onSendTokenUserId(
                    userToken = isLoginTeacher.userToken,
                    teacherId = isLoginTeacher.teacherId
                )
                    _state.update {state -> true }
                }
                else{
                    _state.update {state -> false }
                }
            }
            catch (e : Exception){
                Log.d( TAG , "error => ${e.message}")
            }
        }
    }

}