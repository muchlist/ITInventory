package com.meretas.itinventory.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ServerPostData(
    val token: String,
    val serverName: String,
    val ipAddress: String?,
    val category: String,
    val location: String?,
    val year: String?,
    val merkModel: String?,
    val status: String,
    val note: String?
) : Parcelable