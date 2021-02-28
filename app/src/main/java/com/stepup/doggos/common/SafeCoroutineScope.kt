package com.stepup.doggos.common

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

class PassthroughExceptionHandler : AbstractCoroutineContextElement(CoroutineExceptionHandler), CoroutineExceptionHandler {

    private val mutableErrors = Channel<Throwable>(Channel.BUFFERED)
    val errors: Flow<Throwable> = mutableErrors.receiveAsFlow()

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        mutableErrors.offer(exception)
    }
}

fun CoroutineContext.withChildJob(supervisor: Boolean = true): CoroutineContext {
    return this + if (supervisor) { SupervisorJob(this[Job]) } else Job(this[Job])
}