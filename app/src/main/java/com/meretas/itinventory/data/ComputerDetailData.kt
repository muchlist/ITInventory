package com.meretas.itinventory.data


import com.google.gson.annotations.SerializedName

data class ComputerDetailData(
    @SerializedName("author")
    val author: String,
    @SerializedName("branch")
    val branch: String,
    @SerializedName("client_name")
    val clientName: String,
    @SerializedName("computer_type")
    val computerType: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("division")
    val division: String,
    @SerializedName("hardisk")
    val hardisk: Int,
    @SerializedName("hostname")
    val hostname: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("inventory_number")
    val inventoryNumber: String,
    @SerializedName("ip_address")
    val ipAddress: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("_low_spec")
    val lowSpec: Boolean,
    @SerializedName("merk_model")
    val merkModel: String,
    @SerializedName("note")
    val note: String,
    @SerializedName("operation_system")
    val operationSystem: String,
    @SerializedName("processor")
    val processor: Double,
    @SerializedName("ram")
    val ram: Double,
    @SerializedName("seat_management")
    val seatManagement: Boolean,
    @SerializedName("status")
    val status: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("vga_card")
    val vgaCard: String,
    @SerializedName("year")
    val year: String
)