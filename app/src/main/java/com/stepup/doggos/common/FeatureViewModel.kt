package com.stepup.doggos.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

interface FeatureFactory<Seed, Feature> {
    fun getFeatureFor(seed: Seed): Feature
}

fun <Seed, Feature> CoroutineScope.featureFactory(
    makeNewFeature: CoroutineScope.(Seed) -> Feature
) = object : FeatureFactory<Seed, Feature> {

    private fun makeNewScope() = CoroutineScope(coroutineContext.withChildJob())
    private var coroutineScope: CoroutineScope? = null

    private var seed: Seed? = null
    private var feature: Feature? = null

    override fun getFeatureFor(seed: Seed): Feature {
        return feature?.takeIf { this.seed == seed } ?: run {
            this.seed = seed
            coroutineScope?.cancel()
            val scope = makeNewScope().also { coroutineScope = it }
            scope.makeNewFeature(seed).also {
                this.feature = it
            }
        }
    }
}

fun <Feature> CoroutineScope.featureFactory(
    makeNewFeature: CoroutineScope.() -> Feature
) = featureFactory<Unit, Feature> { makeNewFeature() }

val <Feature> FeatureFactory<Unit, Feature>.feature get() = getFeatureFor(Unit)