package com.stepup.doggos.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

inline fun <T> MutableStateFlow<T>.update(mutation: T.() -> T) {
    value = value.let(mutation)
}

inline fun <T> Flow<T>.onLoading(crossinline block: FlowCollector<T>.(isLoading: Boolean) -> Unit): Flow<T> {
    return onStart { block(true) }.onCompletion { block(false) }
}

