package com.stepup.doggos.list.domain

import com.stepup.doggos.model.Doggo
import com.stepup.doggos.model.DoggosListItem
import com.stepup.doggos.common.update
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

interface DoggosListFeature {
    val state: StateFlow<DoggosListState>
    val errors: Flow<DoggosListError>
    val navigation: Flow<DoggosListToDetails>
    fun openDetails(id: Doggo.Id)
}

data class DoggosListState(
    val list: List<DoggosListItem>? = null
)

data class DoggosListToDetails(val id: Doggo.Id)

data class DoggosListError(val message: String? = null)

fun DoggosListFeature(
    scope: CoroutineScope,
    list: Flow<List<DoggosListItem>>
): DoggosListFeature = object : DoggosListFeature {

    override val state = MutableStateFlow(DoggosListState())

    private val mutableErrors = Channel<DoggosListError>(Channel.BUFFERED)
    override val errors: Flow<DoggosListError> = mutableErrors.receiveAsFlow()

    private val mutableNavigation = Channel<DoggosListToDetails>(Channel.BUFFERED)
    override val navigation = mutableNavigation.receiveAsFlow()

    init {
        list.onEach {
            state.update { copy(list = it) }
        }.catch {
            mutableErrors.send(DoggosListError())
        }.launchIn(scope)
    }

    override fun openDetails(id: Doggo.Id) {
        mutableNavigation.offer(DoggosListToDetails(id))
    }
}