package com.stepup.doggos.details.domain

import com.stepup.doggos.model.Doggo
import com.stepup.doggos.model.DoggoDetailsItem
import com.stepup.doggos.model.Url
import com.stepup.doggos.common.onLoading
import com.stepup.doggos.common.update
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

interface DoggoDetailsFeature {
    val state: StateFlow<DoggoDetailsState>
    val errors: Flow<DoggoDetailsError>
    val navigation: Flow<DoggoDetailsAdopt>
}

data class DoggoDetailsState(
    val doggo: Doggo.Short? = null,
    val details: Doggo.Details? = null,
    val isLoading: Boolean = true,
    val adopt: (() -> Unit)? = null,
)

data class DoggoDetailsAdopt(val adoptUrl: Url)

data class DoggoDetailsError(val message: String? = null)

fun DoggoDetailsFeature(
    scope: CoroutineScope,
    details: Flow<DoggoDetailsItem?>
): DoggoDetailsFeature = object : DoggoDetailsFeature {

    override val state = MutableStateFlow(DoggoDetailsState())

    private val mutableErrors = Channel<DoggoDetailsError>(Channel.BUFFERED)
    override val errors = mutableErrors.receiveAsFlow()

    private val mutableNavigation = Channel<DoggoDetailsAdopt>(Channel.BUFFERED)
    override val navigation = mutableNavigation.receiveAsFlow()

    init {
        details.onEach {
            state.update {
                copy(
                    doggo = it?.doggo,
                    details = it?.details,
                    adopt = it?.details?.url?.let {
                        { mutableNavigation.offer(DoggoDetailsAdopt(it)) }
                    }
                )
            }
        }.onLoading {
            state.update { copy(isLoading = it) }
        }.catch {
            mutableErrors.send(DoggoDetailsError())
        }.launchIn(scope)
    }
}