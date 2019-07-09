package com.meretas.itinventory.login

import com.meretas.itinventory.data.CurrentUserData
import com.meretas.itinventory.data.TokenData
import com.meretas.itinventory.services.Api
import com.meretas.itinventory.utils.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPresenter(private var view: LoginView?) {

    fun getStatusConnection() {

        Api.retrofitService.getCurrentUser("").enqueue(object : Callback<CurrentUserData> {
            override fun onFailure(call: Call<CurrentUserData>, t: Throwable) {
                view?.updateConnection(false)
            }

            override fun onResponse(call: Call<CurrentUserData>, response: Response<CurrentUserData>) {
                if (response.isSuccessful) {
                    view?.updateConnection(true)
                } else if (response.code() == 401) {
                    view?.updateConnection(true)
                } else {
                    view?.updateConnection(false)
                    view?.showToast(response.code().toString())
                }
            }
        })

    }

    fun getToken(username: String, password: String){

        Api.retrofitService.getTokenLogin(username,password).enqueue(object : Callback<TokenData> {
            override fun onFailure(call: Call<TokenData>, t: Throwable) {
                view?.showToast("Gagal login")
            }

            override fun onResponse(call: Call<TokenData>, response: Response<TokenData>) {
                if (response.isSuccessful) {
                    val tokenRespon = response.body()!!
                    App.prefs.authTokenSave = "Token " +tokenRespon.key
                    view?.gotoDashboardActivity()
                } else if (response.code() == 400) {
                    view?.showToast("User atau password salah")
                } else {
                    view?.showToast("Gagal login")
                }
            }
        })


    }

    fun onDestroy() {
        view = null
    }
}