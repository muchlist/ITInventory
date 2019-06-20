package com.meretas.itinventory.data


import com.google.gson.annotations.SerializedName

data class TokenData(
    @SerializedName("key")
    val key: String
)