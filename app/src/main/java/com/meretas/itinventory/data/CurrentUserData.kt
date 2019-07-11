package com.meretas.itinventory.data


import com.squareup.moshi.Json

data class CurrentUserData(
    @Json(name = "first_name")
    val firstName: String,
    @Json(name = "last_name")
    val lastName: String,
    @Json(name = "profile")
    val profile: Profile?,
    @Json(name = "username")
    val username: String
) {
    data class Profile(
        @Json(name = "id")
        val id: Int,
        @Json(name = "is_read_only")
        val isReadOnly: Boolean,
        @Json(name = "user")
        val user: Int,
        @Json(name = "user_branch")
        val userBranch: String,
        @Json(name = "user_regional")
        val userRegional: String
    )
}