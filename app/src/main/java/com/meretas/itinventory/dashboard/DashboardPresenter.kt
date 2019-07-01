package com.meretas.itinventory.dashboard

import com.meretas.itinventory.data.CurrentUserData
import com.meretas.itinventory.data.HistoryListData
import com.meretas.itinventory.services.Api
import com.meretas.itinventory.utils.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DashboardPresenter(private var view: DashboarView?) {

    fun getCurrentUserInfo() {
        Api.retrofitService.getCurrentUser(App.prefs.authToken).enqueue(object : Callback<CurrentUserData> {
            override fun onFailure(call: Call<CurrentUserData>, t: Throwable) {
                // view?.updateConnection(false)
            }

            override fun onResponse(call: Call<CurrentUserData>, response: Response<CurrentUserData>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()!!
                    val name = userResponse.firstName + " " + userResponse.lastName
                    val branch = userResponse.groups[0].name

                    view?.getUserInfo(name, branch)
                } else if (response.code() == 401) {
                    view?.showToast(App.prefs.authToken)
                } else {
                    // view?.updateConnection(false)
                }
            }
        })

    }

    fun getHistoryDashboard() {

        view?.showProgressBarHistory()

        Api.retrofitService.getHistoryDashboard(App.prefs.authToken).enqueue(object : Callback<HistoryListData> {
            override fun onFailure(call: Call<HistoryListData>, t: Throwable) {
                view?.hideProgressBarHistory()
                view?.showToast(t.toString())
                //GAGAL LOAD
            }

            override fun onResponse(call: Call<HistoryListData>, response: Response<HistoryListData>) {
                if (response.isSuccessful) {
                    val historyResponse = response.body()!!
                    val historyList = historyResponse.results

                    view?.hideProgressBarHistory()
                    view?.showHistory(historyList)

                } else if (response.code() == 401) {
                    view?.hideProgressBarHistory()
                    view?.showToast(response.code().toString())
                    //APABILA TOKEN SALAH LEMPAR KE LOGIN

                } else {
                    view?.hideProgressBarHistory()
                    view?.showToast(response.code().toString())
                    //GAGAL LOAD
                }
            }
        })
    }

    fun onDestroy() {
        view = null
    }
}