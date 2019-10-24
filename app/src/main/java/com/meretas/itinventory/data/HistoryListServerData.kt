package com.meretas.itinventory.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

data class HistoryListServerData(
    @Json(name = "count") val count: Int,
    @Json(name = "next") val next: String?,
    @Json(name = "previous") val previous: String?,
    @Json(name = "results") val results: List<Result>
) {
    @Parcelize
    data class Result(
        @Json(name = "author") val author: String,
        @Json(name = "branch") val branch: String,
        @Json(name = "server") val server: String,
        @Json(name = "server_id") val serverId: Int,
        @Json(name = "created_at") val createdAt: String,
        @Json(name = "id") val id: Int,
        @Json(name = "note") val note: String,
        @Json(name = "status_history") val statusHistory: String,
        @Json(name = "updated_at") val updatedAt: String
    ): Parcelable
}