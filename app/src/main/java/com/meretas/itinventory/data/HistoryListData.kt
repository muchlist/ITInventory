package com.meretas.itinventory.data


import com.google.gson.annotations.SerializedName

data class HistoryListData(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String?,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("results")
    val results: List<Result>
) {
    data class Result(
        @SerializedName("author")
        val author: String,
        @SerializedName("branch")
        val branch: String,
        @SerializedName("computer")
        val computer: String,
        @SerializedName("computer_id")
        val computerId: Int,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("note")
        val note: String,
        @SerializedName("status_history")
        val statusHistory: String,
        @SerializedName("updated_at")
        val updatedAt: String
    )
}