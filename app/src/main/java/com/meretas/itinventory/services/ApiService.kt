package com.meretas.itinventory.services

import com.meretas.itinventory.data.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.ResponseBody
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

    //USER
    @FormUrlEncoded
    @POST("rest-auth/login/")
    fun getTokenLogin(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<TokenData>

    @GET("user/")
    fun getCurrentUser(
        @Header("Authorization") token: String
    ): Call<CurrentUserData>

    //COMPUTER
    @GET("computers/")
    fun getComputerList(
        @Header("Authorization") token: String,
        @Query("branch") branch: String,
        @Query("location") location: String = "",
        @Query("division") division: String,
        @Query("seat_management") seat_management: String,
        @Query("status") status: String,
        @Query("ordering") order: String = "division,client_name",
        @Query("format") format: String = "json"
    ): Call<ComputerListData>

    @GET("computers/")
    fun getComputerListSearch(
        @Header("Authorization") token: String,
        @Query("search") search: String?,
        @Query("ordering") order: String = "client_name",
        @Query("format") format: String = "json"
    ): Call<ComputerListData>

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

    //HISTORY COMPUTER
    @FormUrlEncoded
    @POST("computers/{id}/history/")
    fun postHistory(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Field("note") note: String,
        @Field("status_history") status_history: String
    ): Call<HistoryListData.Result>

    @GET("historys/")
    fun getHistoryDashboard(
        @Header("Authorization") token: String
    ): Call<HistoryListData>

    @GET("computers/{id}/historys/")
    fun getHistoryPerComputer(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Query("format") format: String = "json"
    ): Call<HistoryListData>

    //STOCK
    @GET("stocks/")
    fun getStockList(
        @Header("Authorization") token: String,
        @Query("branch") branch: String = "Banjarmasin",
        @Query("category") category: String,
        @Query("active") active: Boolean = true,
        @Query("ordering") order: String = "branch,category",
        @Query("format") format: String = "json"
    ): Call<StockListData>

    @GET("stocks/{id}/")
    fun getStockDetail(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Query("format") format: String = "json"
    ): Call<StockListData.Result>

    @GET("stocks/{id}/{status}")
    fun getChangeStockStatus(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Path("status") status: String
    ): Call<StockListData.Result>

    @FormUrlEncoded
    @POST("stocks/")
    fun postStock(
        @Header("Authorization") token: String,
        @Field("stock_name") stockName: String,
        @Field("category") category: String,
        @Field("threshold") threshold: Int? = 0,
        @Field("unit") unit: String? = "Unit",
        @Field("active") active: Boolean,
        @Field("note") note: String?
    ): Call<StockListData.Result>

    @FormUrlEncoded
    @PUT("stocks/{id}/")
    fun putStock(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Field("stock_name") stockName: String,
        @Field("category") category: String,
        @Field("threshold") threshold: Int? = 0,
        @Field("unit") unit: String? = "Unit",
        @Field("active") active: Boolean,
        @Field("note") note: String?
    ): Call<StockListData.Result>

    @GET("stocks/{id}/additions/")
    fun getAdditionList(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Query("format") format: String = "json"
    ): Call<AddAndConsumeData>

    @GET("stocks/{id}/consumes/")
    fun getConsumeList(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Query("format") format: String = "json"
    ): Call<AddAndConsumeData>

    @FormUrlEncoded
    @POST("stocks/{id}/consume/")
    fun postConsume(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Field("event_number") eventNumber: String,
        @Field("note") note: String,
        @Field("qty") qty: Int? = 0
    ): Call<AddAndConsumeData.Result>

    @FormUrlEncoded
    @POST("stocks/{id}/addition/")
    fun postAddition(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Field("event_number") eventNumber: String,
        @Field("note") note: String,
        @Field("qty") qty: Int? = 0
    ): Call<AddAndConsumeData.Result>

    @FormUrlEncoded
    @PUT("consumes/{id}/")
    fun putConsume(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Field("event_number") eventNumber: String,
        @Field("note") note: String,
        @Field("qty") qty: Int? = 0
    ): Call<AddAndConsumeData.Result>

    @FormUrlEncoded
    @PUT("additions/{id}/")
    fun putAddition(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Field("event_number") eventNumber: String,
        @Field("note") note: String,
        @Field("qty") qty: Int? = 0
    ): Call<AddAndConsumeData.Result>

    @DELETE("additions/{id}/")
    fun deleteAddition(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Call<ResponseBody>

    @DELETE("consumes/{id}/")
    fun deleteConsume(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Call<ResponseBody>

    @GET("additions/{id}/")
    fun getAdditionDetail(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Query("format") format: String = "json"
    ): Call<AddAndConsumeData.Result>

    @GET("consumes/{id}/")
    fun getConsumeDetail(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Query("format") format: String = "json"
    ): Call<AddAndConsumeData.Result>

    //CCTV
    @GET("cctvs/")
    fun getCctvList(
        @Header("Authorization") token: String,
        @Query("branch") branch: String,
        @Query("status") status: String,
        @Query("ordering") order: String = "branch,cctv_name",
        @Query("format") format: String = "json"
    ): Call<CctvListData>

    @GET("cctvs/")
    fun getCctvListSearch(
        @Header("Authorization") token: String,
        @Query("search") search: String?,
        @Query("ordering") order: String = "branch,cctv_name",
        @Query("format") format: String = "json"
    ): Call<CctvListData>

    @FormUrlEncoded
    @POST("cctvs/")
    fun postCctv(
        @Header("Authorization") token: String,
        @Field("cctv_name") cctvName: String,
        @Field("ip_address") ipAddress: String?,
        @Field("location") location: String? = "None",
        @Field("year") year: String?,
        @Field("merk_model") merkModel: String?,
        @Field("status") status: String? = "Aktif",
        @Field("note") note: String?
    ): Call<CctvListData.Result>


}

object Api {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}