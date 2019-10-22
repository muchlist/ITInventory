package com.meretas.itinventory.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PrinterPostData(
    val token: String,
    val printerName: String,
    val ipAddress: String?,
    val division: String,
    val year: String?,
    val merkModel: String?,
    val status: String,
    val note: String?
) : Parcelable