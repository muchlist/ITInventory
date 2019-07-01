package com.meretas.itinventory.data


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

data class ComputerListData(
    @Json(name = "count") val count: Int,
    @Json(name = "next") val next: String?,
    @Json(name = "previous") val previous: String?,
    @Json(name = "results") val results: List<Result>
) {
    @Parcelize
    data class Result(
        @Json(name = "author") val author: String,
        @Json(name = "branch") val branch: String,
        @Json(name = "client_name") val clientName: String,
        @Json(name = "computer_type") val computerType: String,
        @Json(name = "created_at") val createdAt: String,
        @Json(name = "division") val division: String,
        @Json(name = "hardisk") val hardisk: Int,
        @Json(name = "hostname") val hostname: String,
        @Json(name = "id") val id: Int,
        @Json(name = "inventory_number") val inventoryNumber: String,
        @Json(name = "ip_address") val ipAddress: String,
        @Json(name = "location") val location: String,
        @Json(name = "_low_spec") val lowSpec: Boolean,
        @Json(name = "merk_model") val merkModel: String,
        @Json(name = "note") val note: String,
        @Json(name = "operation_system") val operationSystem: String,
        @Json(name = "processor") val processor: Double,
        @Json(name = "ram") val ram: Double,
        @Json(name = "seat_management") val seatManagement: Boolean,
        @Json(name = "status") val status: String,
        @Json(name = "updated_at") val updatedAt: String,
        @Json(name = "vga_card") val vgaCard: String,
        @Json(name = "year") val year: String
    ): Parcelable
}