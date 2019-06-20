package com.meretas.itinventory.data


import com.google.gson.annotations.SerializedName

data class StatusCheckData(
    @SerializedName("computers")
    val computers: String
)