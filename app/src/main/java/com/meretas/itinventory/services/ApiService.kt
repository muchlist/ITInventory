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
    ): Call<HistoryListGeneralData.Result>

    @GET("historys/")
    fun getHistoryDashboard(
        @Header("Authorization") token: String
    ): Call<HistoryListGeneralData>

    @GET("computers/{id}/historys/")
    fun getHistoryPerComputer(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Query("format") format: String = "json"
    ): Call<HistoryListGeneralData>

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

    @GET("cctvs/{id}")
    fun getCctvRefresh(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Query("format") format: String = "json"
    ): Call<CctvListData.Result>

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

    @FormUrlEncoded
    @PUT("cctvs/{id}/")
    fun putCctv(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Field("cctv_name") cctvName: String,
        @Field("ip_address") ipAddress: String?,
        @Field("location") location: String?,
        @Field("year") year: String?,
        @Field("merk_model") merkModel: String?,
        @Field("status") status: String? = "Aktif",
        @Field("note") note: String?
    ): Call<CctvListData.Result>

    //HISTORY CCTV
    @FormUrlEncoded
    @POST("cctvs/{id}/history/")
    fun postHistoryCctv(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Field("note") note: String,
        @Field("status_history") status_history: String
    ): Call<HistoryListCctvData.Result>

    @GET("cctv-historys/")
    fun getHistoryCctvDashboard(
        @Header("Authorization") token: String
    ): Call<HistoryListCctvData>

    @GET("cctvs/{id}/historys/")
    fun getHistoryPerCctv(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Query("format") format: String = "json"
    ): Call<HistoryListCctvData>


    //Printer
    @GET("printers/")
    fun getPrinterList(
        @Header("Authorization") token: String,
        @Query("branch") branch: String,
        @Query("division") division: String,
        @Query("status") status: String,
        @Query("ordering") order: String = "branch,division,printer_name",
        @Query("format") format: String = "json"
    ): Call<PrinterListData>

    @GET("printers/")
    fun getPrinterListSearch(
        @Header("Authorization") token: String,
        @Query("search") search: String?,
        @Query("ordering") order: String = "branch,division,printer_name",
        @Query("format") format: String = "json"
    ): Call<PrinterListData>

    @GET("printers/{id}")
    fun getPrinterRefresh(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Query("format") format: String = "json"
    ): Call<PrinterListData.Result>

    @FormUrlEncoded
    @POST("printers/")
    fun postPrinter(
        @Header("Authorization") token: String,
        @Field("printer_name") printerName: String,
        @Field("ip_address") ipAddress: String?,
        @Field("division") division: String,
        @Field("year") year: String?,
        @Field("merk_model") merkModel: String?,
        @Field("status") status: String? = "Baik",
        @Field("note") note: String?
    ): Call<PrinterListData.Result>

    @FormUrlEncoded
    @PUT("printers/{id}/")
    fun putPrinter(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Field("printer_name") printerName: String,
        @Field("ip_address") ipAddress: String?,
        @Field("division") division: String,
        @Field("year") year: String?,
        @Field("merk_model") merkModel: String?,
        @Field("status") status: String? = "Baik",
        @Field("note") note: String?
    ): Call<PrinterListData.Result>

    //HISTORY PRINTER
    @FormUrlEncoded
    @POST("printers/{id}/history/")
    fun postHistoryPrinter(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Field("note") note: String,
        @Field("status_history") status_history: String
    ): Call<HistoryListPrinterData.Result>

    @GET("printer-historys/")
    fun getHistoryPrinterDashboard(
        @Header("Authorization") token: String
    ): Call<HistoryListPrinterData>

    @GET("printers/{id}/historys/")
    fun getHistoryPerPrinter(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Query("format") format: String = "json"
    ): Call<HistoryListPrinterData>

    //SERVER
    @GET("servers/")
    fun getServerList(
        @Header("Authorization") token: String,
        @Query("branch") branch: String,
        @Query("status") status: String,
        @Query("category") category: String,
        @Query("ordering") order: String = "branch,category,server_name",
        @Query("format") format: String = "json"
    ): Call<ServerListData>

    @GET("servers/")
    fun getServerListSearch(
        @Header("Authorization") token: String,
        @Query("search") search: String?,
        @Query("ordering") order: String = "branch,category,server_name",
        @Query("format") format: String = "json"
    ): Call<ServerListData>

    @GET("servers/{id}")
    fun getServerRefresh(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Query("format") format: String = "json"
    ): Call<ServerListData.Result>

    @FormUrlEncoded
    @POST("servers/")
    fun postServer(
        @Header("Authorization") token: String,
        @Field("server_name") serverName: String,
        @Field("ip_address") ipAddress: String?,
        @Field("category") category: String,
        @Field("location") location: String?,
        @Field("year") year: String?,
        @Field("merk_model") merkModel: String?,
        @Field("status") status: String? = "Aktif",
        @Field("note") note: String?
    ): Call<ServerListData.Result>

    @FormUrlEncoded
    @PUT("servers/{id}/")
    fun putServer(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Field("server_name") serverName: String,
        @Field("ip_address") ipAddress: String?,
        @Field("category") category: String,
        @Field("location") location: String?,
        @Field("year") year: String?,
        @Field("merk_model") merkModel: String?,
        @Field("status") status: String? = "Aktif",
        @Field("note") note: String?
    ): Call<ServerListData.Result>

    //HISTORY SERVER
    @FormUrlEncoded
    @POST("servers/{id}/history/")
    fun postHistoryServer(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Field("note") note: String,
        @Field("status_history") status_history: String
    ): Call<HistoryListServerData.Result>

    @GET("server-historys/")
    fun getHistoryServerDashboard(
        @Header("Authorization") token: String
    ): Call<HistoryListServerData>

    @GET("servers/{id}/historys/")
    fun getHistoryPerServer(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Query("format") format: String = "json"
    ): Call<HistoryListServerData>

}

object Api {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}