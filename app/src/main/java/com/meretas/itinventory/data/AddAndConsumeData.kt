package com.meretas.itinventory.data


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

data class AddAndConsumeData(
    @Json(name = "count")
    val count: Int,
    @Json(name = "next")
    val next: String?,
    @Json(name = "previous")
    val previous: String?,
    @Json(name = "results")
    val results: List<Result>
) {
    @Parcelize
    data class Result(
        @Json(name = "author")
        val author: String,
        @Json(name = "branch")
        val branch: String,
        @Json(name = "category")
        val category: String,
        @Json(name = "created_at")
        val createdAt: String,
        @Json(name = "event_number")
        val eventNumber: String?,
        @Json(name = "id")
        val id: Int,
        @Json(name = "note")
        val note: String?,
        @Json(name = "qty")
        val qty: Int,
        @Json(name = "stock")
        val stock: String,
        @Json(name = "stock_id")
        val stockId: Int,
        @Json(name = "unit")
        val unit: String,
        @Json(name = "updated_at")
        val updatedAt: String
    ): Parcelable
}