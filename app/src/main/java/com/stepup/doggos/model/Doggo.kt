package com.stepup.doggos.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

object Doggo {

    @JvmInline
    @Parcelize
    value class Id(val id: String) : Parcelable

    data class Short(
        val name: String,
        val age: IntRange,
        val imageUrl: Url
    )

    data class Details(
        val sex: Sex,
        val breed: Breed,
        val likes: String?,
        val typeOfHome: String?,
        val aboutMe: String?,
        val url: Url
    )

    @JvmInline
    value class Breed(
        val name: String
    )

    enum class Sex {
        Male, Female
    }
}

@JvmInline
value class Url(val url: String)