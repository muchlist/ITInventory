package com.meretas.itinventory.data


import com.squareup.moshi.Json

data class CurrentUserData(
    @Json(name = "first_name") val firstName: String,
    @Json(name = "groups") val groups: List<Group>,
    @Json(name = "last_name") val lastName: String,
    @Json(name = "username") val username: String
) {
    data class Group(
        @Json(name = "id") val id: Int,
        @Json(name = "name") val name: String,
        @Json(name = "permissions") val permissions: List<Any>
    )
}