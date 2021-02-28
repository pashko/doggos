package com.stepup.doggos.details.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stepup.doggos.details.domain.DoggoDetailsFeature
import com.stepup.doggos.model.Doggo
import com.stepup.doggos.model.DoggosRepo
import com.stepup.doggos.common.featureFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DoggoDetailsViewModel @Inject constructor(
    private val repo: DoggosRepo,
) : ViewModel() {

    private val featureFactory = viewModelScope.featureFactory<Doggo.Id, DoggoDetailsFeature> { id ->
        DoggoDetailsFeature(this, repo.details(id))
    }

    fun getFeatureFor(id: Doggo.Id) = featureFactory.getFeatureFor(id)
}