package com.meretas.itinventory.data


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

data class StockListData(
    @Json(name = "count") val count: Int,
    @Json(name = "next") val next: String?,
    @Json(name = "previous") val previous: String?,
    @Json(name = "results") val results: List<Result>
) {
    @Parcelize
    data class Result(
        @Json(name = "active") val active: Boolean,
        @Json(name = "author") val author: String,
        @Json(name = "branch") val branch: String,
        @Json(name = "category") val category: String,
        @Json(name = "created_at") val createdAt: String,
        @Json(name = "id") val id: Int,
        @Json(name = "note") val note: String,
        @Json(name = "stock_added") val stockAdded: Int?,
        @Json(name = "stock_name") val stockName: String,
        @Json(name = "stock_used") val stockUsed: Int?,
        @Json(name = "threshold") val threshold: Int,
        @Json(name = "unit") val unit: String,
        @Json(name = "updated_at") val updatedAt: String
    ): Parcelable
}