package com.meretas.itinventory.data


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

data class PrinterListData(
    @Json(name = "count")
    val count: Int,
    @Json(name = "next")
    val next: Int?,
    @Json(name = "previous")
    val previous: Int?,
    @Json(name = "results")
    val results: List<Result>
) { @Parcelize
    data class Result(
        @Json(name = "branch")
        val branch: String,
        @Json(name = "created_at")
        val createdAt: String,
        @Json(name = "division")
        val division: String,
        @Json(name = "id")
        val id: Int,
        @Json(name = "ip_address")
        val ipAddress: String?,
        @Json(name = "merk_model")
        val merkModel: String?,
        @Json(name = "note")
        val note: String?,
        @Json(name = "printer_name")
        val printerName: String,
        @Json(name = "status")
        val status: String,
        @Json(name = "updated_at")
        val updatedAt: String,
        @Json(name = "year")
        val year: String?
    ) : Parcelable
}