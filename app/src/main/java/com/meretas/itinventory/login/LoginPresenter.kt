package com.meretas.itinventory.login

import com.meretas.itinventory.data.CurrentUserData
import com.meretas.itinventory.services.ApiService
import com.meretas.itinventory.utils.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPresenter(
    private var view: LoginView?,
    private val apiService: ApiService
) {

    fun getStatusConnection() {
        val requestCall: Call<CurrentUserData> = apiService.getCurrentUser(App.prefs.authToken)
        requestCall.enqueue(object : Callback<CurrentUserData> {

            override fun onResponse(call: Call<CurrentUserData>, response: Response<CurrentUserData>) {
                if (response.isSuccessful) {
//                    val headerList: List<HalamanHeader> = response.body()!!
//                    val imgHeader = headerList[0].img_halaman
//                    val textHeader = headerList[0].isi_halaman
                    view?.updateConnection(true)
                } else if (response.code() == 403) {
                    view?.updateConnection(true)
                } else {
                    view?.updateConnection(false)
                }
            }

            override fun onFailure(call: Call<CurrentUserData>, t: Throwable) {
                view?.updateConnection(false)
            }
        })

    }

    fun getToken() {

    }

    fun onDestroy() {
        view = null
    }
}