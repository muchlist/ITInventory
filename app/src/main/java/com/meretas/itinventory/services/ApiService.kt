package com.meretas.itinventory.services

import com.meretas.itinventory.data.ComputerListData
import com.meretas.itinventory.data.CurrentUserData
import com.meretas.itinventory.data.HistoryListData
import com.meretas.itinventory.data.TokenData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://inventarispc.herokuapp.com/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ApiService {

    @FormUrlEncoded
    @POST("rest-auth/login/")
    fun getTokenLogin(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<TokenData>

    //@Headers("Content-type:application/json")
    @FormUrlEncoded
    @POST("computers/{id}/history/")
    fun postHistory(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Field("note") note: String,
        @Field("status_history") status_history: String
    ): Call<HistoryListData.Result>

    @FormUrlEncoded
    @POST("computers/")
    fun postComputer(
        @Header("Authorization") token: String,
        @Field("client_name") clientName: String,
        @Field("hostname") hostName: String?,
        @Field("ip_address") ipAddress: String?,
        @Field("inventory_number") inventoryNumber: String?,
        @Field("location") location: String? = "None",
        @Field("division") division: String,
        @Field("seat_management") seatManagement: Boolean = false,
        @Field("year") year: String?,
        @Field("merk_model") merkModel: String?,
        @Field("computer_type") computerType: String? = "Desktop",
        @Field("processor") processor: Double? = 3.0,
        @Field("ram") ram: Double? = 4.0,
        @Field("hardisk") hardisk: Int? = 1000,
        @Field("vga_card") vgaCard: String?,
        @Field("operation_system") operationSystem: String? = "1064",
        @Field("status") status: String? = "Baik",
        @Field("note") note: String?
    ): Call<ComputerListData.Result>

    @FormUrlEncoded
    @PUT("computers/{id}/")
    fun putComputer(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Field("client_name") clientName: String,
        @Field("hostname") hostName: String?,
        @Field("ip_address") ipAddress: String?,
        @Field("inventory_number") inventoryNumber: String?,
        @Field("location") location: String? = "None",
        @Field("division") division: String,
        @Field("seat_management") seatManagement: Boolean = false,
        @Field("year") year: String?,
        @Field("merk_model") merkModel: String?,
        @Field("computer_type") computerType: String? = "Desktop",
        @Field("processor") processor: Double? = 3.0,
        @Field("ram") ram: Double? = 4.0,
        @Field("hardisk") hardisk: Int? = 1000,
        @Field("vga_card") vgaCard: String?,
        @Field("operation_system") operationSystem: String? = "1064",
        @Field("status") status: String? = "Baik",
        @Field("note") note: String?
    ): Call<ComputerListData.Result>

    //@Headers("Content-type:application/json")
    @GET("user/")
    fun getCurrentUser(
        @Header("Authorization") token: String
    ): Call<CurrentUserData>

    //@Headers("Content-type:application/json")
    @GET("historys/")
    fun getHistoryDashboard(
        @Header("Authorization") token: String
    ): Call<HistoryListData>

    //@Headers("Content-type:application/json")
    @GET("computers/{id}/historys/")
    fun getHistoryPerComputer(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Call<HistoryListData>

    //@Headers("Content-type:application/json")
    @GET("computers/")
    fun getComputerList(
        @Header("Authorization") token: String,
        @Query("branch") branch: String = "Banjarmasin",
        @Query("location") location: String = "",
        @Query("ordering") order: String = "division,client_name"
    ): Call<ComputerListData>

    //@Headers("Content-type:application/json")
    @GET("computers/")
    fun getComputerListSearch(
        @Header("Authorization") token: String,
        @Query("search") search: String?,
        @Query("ordering") order: String = "client_name"
    ): Call<ComputerListData>

}

object Api {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}


/*interface ApiService {

    @FormUrlEncoded
    @POST("rest-auth/login/")
    fun getTokenLogin(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<TokenData>

    @Headers("Content-Type:application/json; charset=UTF-8")
    @GET("user/")
    fun getCurrentUser(@Header("Authorization") token : String): Call<CurrentUserData>

}*/