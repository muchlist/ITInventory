package com.meretas.itinventory.data


import com.squareup.moshi.Json

data class AddAndConsumePostData(
    @Json(name = "event_number") val eventNumber: String,
    @Json(name = "note") val note: String,
    @Json(name = "qty") val qty: Int?
)