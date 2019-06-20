package com.meretas.itinventory.services

import com.meretas.itinventory.data.CurrentUserData
import com.meretas.itinventory.data.TokenData
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("rest-auth/login")
    fun getTokenLogin(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<TokenData>

    @Headers("Content-Type:application/json; charset=UTF-8")
    @GET("user/")
    fun getCurrentUser(@Header("Authorization") token : String): Call<CurrentUserData>

}