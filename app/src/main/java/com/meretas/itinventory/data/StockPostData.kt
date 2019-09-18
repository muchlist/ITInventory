package com.meretas.itinventory.data

data class StockPostData (
    val stockName: String,
    val category: String,
    val threshold: Int? = 0,
    val unit: String? = "Unit",
    val active: Boolean,
    val note: String?,
    val id: Int
)