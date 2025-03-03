package com.example.attendancetaker

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


val Context.myPreferenceDataStore: DataStore<Preferences> by preferencesDataStore(name = "my_preference")

data class PreferenceData(
    val userToken: String,
    val teacherId: String,
    val teacherName: String,
)

@Singleton
class MySharedPreferenceDataStore @Inject constructor(
    @ApplicationContext context: Context
) {
    private val myPreferenceDataStore = context.myPreferenceDataStore


    private object PreferenceKeys {
        val USER_TOKEN = stringPreferencesKey("user_token")
        val TEACHER_ID = stringPreferencesKey("teacher_id")
        val TEACHER_NAME = stringPreferencesKey("teacher_name")
    }

    private val preferenceDataFlow = myPreferenceDataStore.data
        .catch {exception ->
            if (exception is IOException){
                emit(emptyPreferences())
            }
            else{
                throw exception
            }
        }
        .map { preference ->
            val userToken : String = preference[PreferenceKeys.USER_TOKEN] ?: ""
            val teacherId = preference[PreferenceKeys.TEACHER_ID] ?: ""
            val teacherName : String = preference[PreferenceKeys.TEACHER_NAME] ?: ""

            PreferenceData(
                userToken = userToken,
                teacherId = teacherId,
                teacherName = teacherName
            )

        }

    suspend fun onSendTokenUserId(userToken : String , teacherId : String){
        myPreferenceDataStore.edit {mutablePreference ->
            mutablePreference[PreferenceKeys.USER_TOKEN] = userToken
            mutablePreference[PreferenceKeys.TEACHER_ID] = teacherId
        }
    }

    suspend fun onSendTeacherName(teacherName : String){
        myPreferenceDataStore.edit {mutablePreference ->
        mutablePreference[PreferenceKeys.TEACHER_NAME] = teacherName
        }
    }


    suspend fun getUserToken() : String {
        return  preferenceDataFlow.map {
            it.userToken
        }.first()
    }

    suspend fun getTeacherId() : String {
        return preferenceDataFlow.map {
            it.teacherId
        }.first()
    }

    suspend fun getTeacherName() : String {
        return preferenceDataFlow.map {
            it.teacherName
        }.first()
    }


    suspend fun onClean() {
        myPreferenceDataStore.edit {preference ->
            preference.clear()
        }
    }

}