package com.meretas.itinventory.data


import com.squareup.moshi.Json

data class TokenData(
    @Json(name = "key") val key: String
)