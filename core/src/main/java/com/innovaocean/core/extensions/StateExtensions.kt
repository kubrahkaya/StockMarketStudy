package com.innovaocean.core.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@Suppress("LongParameterList")
fun <T1, T2, R> combineInState(
    scope: CoroutineScope,
    initialState: R,
    flow: Flow<T1>,
    flow2: Flow<T2>,
    sharingStarted: SharingStarted = SharingStarted.WhileSubscribed(),
    transform: suspend (a: T1, b: T2) -> R
): StateFlow<R> = flow.combine(flow2, transform).stateIn(scope, sharingStarted, initialState)


@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun <T> StateFlow<T>.collectAsStateDefault(
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.RESUMED,
    context: CoroutineContext = EmptyCoroutineContext
): State<T> =
    collectAsStateWithLifecycle(
        lifecycle = lifecycle,
        minActiveState = minActiveState,
        context = context
    )
