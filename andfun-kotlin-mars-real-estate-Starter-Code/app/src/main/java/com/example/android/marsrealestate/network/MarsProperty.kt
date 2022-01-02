package com.example.android.marsrealestate.network

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MarsProperty(
        val id: String,
        val type: String,
        val price: Double,
        @Json(name = "img_src")
        val imageSrc: String

) : Parcelable {
    val isRental get() = type == "rent"
}
