package com.stepup.doggos.model

import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DoggosRepo @Inject constructor(
    private val dao: DoggosDao
) {

    val list get() = flow {
        emit(dao.list().map { DoggosListItem(it.key, it.value) })
    }

    fun details(id: Doggo.Id) = flow {
        emit(DoggoDetailsItem(
            id = id,
            doggo = dao.getShortFor(id) ?: return@flow,
            details = dao.getDetailsFor(id)
        ))
    }
}

data class DoggosListItem(
    val id: Doggo.Id,
    val doggo: Doggo.Short
)

data class DoggoDetailsItem(
    val id: Doggo.Id,
    val doggo: Doggo.Short,
    val details: Doggo.Details?
)