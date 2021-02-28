package com.stepup.doggos.list.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stepup.doggos.common.feature
import com.stepup.doggos.common.featureFactory
import com.stepup.doggos.list.domain.DoggosListFeature
import com.stepup.doggos.model.DoggosRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DoggosListViewModel @Inject constructor(
    private val repo: DoggosRepo,
) : ViewModel() {

    private val featureFactory = viewModelScope.featureFactory<DoggosListFeature> {
        DoggosListFeature(this, repo.list)
    }

    val feature get() = featureFactory.feature
}