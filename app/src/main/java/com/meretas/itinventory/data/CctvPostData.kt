package com.meretas.itinventory.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CctvPostData(
    val token: String,
    val cctvName: String,
    val ipAddress: String?,
    val location: String?,
    val year: String?,
    val merkModel: String?,
    val status: String,
    val note: String?
) : Parcelable