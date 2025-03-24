package com.example.attendancetaker.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Composable
fun <T>ObserverAsEvent(
    flow: Flow<T>,
    key1 : Any? = null,
    key2 : Any? = null,
    onEvent : (T) -> Unit
){
    // It observe the flow of fragment or activity (Only ui was active)
    val lifecycleOwner = LocalLifecycleOwner.current

    // It trigger when any of the changes it observe in ui
    LaunchedEffect(lifecycleOwner.lifecycle , key1 , key2 , flow) {

        // It helps to stay in started state and work only on active state
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){

            // It helps to work on main UI thread with immediate action without any unnecessary dispatching
            withContext(Dispatchers.Main.immediate){

                // Flow is collected and emit in onEvent state when their is any change in ui , it update immediately
                flow.collect(onEvent)
            }
        }
    }
}