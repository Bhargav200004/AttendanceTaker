package com.example.attendancetaker.utils

sealed class Result<out T>(){

    data class OnSuccess<out R>(val data : R?) : Result<R>()

    data class OnError<out E>(val error : E?) : Result<E>()

}

