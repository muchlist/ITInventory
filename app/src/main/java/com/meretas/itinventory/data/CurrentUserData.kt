package com.meretas.itinventory.data


import com.google.gson.annotations.SerializedName

data class CurrentUserData(
    @SerializedName("groups")
    val groups: List<Group>,
    @SerializedName("username")
    val username: String
) {
    data class Group(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("permissions")
        val permissions: List<Any>
    )
}