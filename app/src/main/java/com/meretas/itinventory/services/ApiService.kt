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

    @Headers("Content-type:application/json")
    @GET("user/")
    fun getCurrentUser(
        @Header("Authorization") token: String
    ): Call<CurrentUserData>

    @Headers("Content-type:application/json")
    @GET("historys/")
    fun getHistoryDashboard(
        @Header("Authorization") token: String
    ): Call<HistoryListData>

    @Headers("Content-type:application/json")
    @GET("computers/")
    fun getComputerList(
        @Header("Authorization") token: String,
        @Query("branch") branch: String = "Banjarmasin",
        @Query("location") location: String = "",
        @Query("ordering") order: String = "division,client_name"
    ): Call<ComputerListData>

    @Headers("Content-type:application/json")
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