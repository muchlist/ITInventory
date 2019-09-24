package com.meretas.itinventory.login

import com.meretas.itinventory.data.CurrentUserData
import com.meretas.itinventory.data.TokenData
import com.meretas.itinventory.services.Api
import com.meretas.itinventory.utils.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPresenter(private var view: LoginView?) {

    fun getToken(username: String, password: String) {
        view?.showLoading()
        Api.retrofitService.getTokenLogin(username, password).enqueue(object : Callback<TokenData> {
            override fun onFailure(call: Call<TokenData>, t: Throwable) {
                view?.showToast("Gagal login")
                view?.hideLoading()
            }

            override fun onResponse(call: Call<TokenData>, response: Response<TokenData>) {
                when {
                    response.isSuccessful -> {
                        val tokenRespon = response.body()!!
                        val pref = App.prefs
                        //Jika auth token satu sudah ada maka isi auth token 2
                        if (pref.authTokenOne.isEmpty()) {
                            pref.authTokenSave = "Token " + tokenRespon.key
                            pref.authTokenOne = "Token " + tokenRespon.key
                        } else {
                            pref.authTokenSave = "Token " + tokenRespon.key
                            pref.authTokenTwo = "Token " + tokenRespon.key
                        }
                        view?.gotoDashboardActivity()
                    }
                    response.code() == 400 -> {
                        view?.showToast("User atau password salah")
                        view?.hideLoading()
                    }
                    else -> {
                        view?.showToast("Gagal login")
                        view?.hideLoading()
                    }
                }
            }
        })


    }

    fun getStatusConnection() {

        Api.retrofitService.getCurrentUser("").enqueue(object : Callback<CurrentUserData> {
            override fun onFailure(call: Call<CurrentUserData>, t: Throwable) {
                view?.updateConnection(false)
            }

            override fun onResponse(
                call: Call<CurrentUserData>,
                response: Response<CurrentUserData>
            ) {
                when {
                    response.isSuccessful -> view?.updateConnection(true)
                    response.code() == 401 -> view?.updateConnection(true)
                    else -> {
                        view?.updateConnection(false)
                        view?.showToast(response.code().toString())
                    }
                }
            }
        })

    }

    fun onDestroy() {
        view = null
    }
}