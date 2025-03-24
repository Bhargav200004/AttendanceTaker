package com.example.attendancetaker.utils

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object SnackBarController {

    private val _event = Channel<SnackBarEvent>()
    val event = _event.receiveAsFlow()

    suspend fun sendEvent(event: SnackBarEvent) {
        _event.send(event)
    }

}


data class SnackBarEvent(
    val message: String,
    val actionLabel: SnackBarAction? = null
)

data class SnackBarAction(
    val label: String?,
    val action: () -> Unit
)