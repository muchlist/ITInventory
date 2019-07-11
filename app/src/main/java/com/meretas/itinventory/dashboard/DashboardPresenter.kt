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
        Api.retrofitService.getCurrentUser(App.prefs.authTokenSave).enqueue(object : Callback<CurrentUserData> {
            override fun onFailure(call: Call<CurrentUserData>, t: Throwable) {

            }

            override fun onResponse(call: Call<CurrentUserData>, response: Response<CurrentUserData>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()!!
                    val name = userResponse.firstName + " " + userResponse.lastName
                    val branch = userResponse.profile?.userBranch ?: "Profil Null"
                    val isReadOnly = userResponse.profile?.isReadOnly ?: true

                    view?.getUserInfo(name, branch, isReadOnly)
                    App.prefs.isCompleteLogin = true
                } else if (response.code() == 401) {
                    App.prefs.authTokenSave = ""
                    view?.showToast(response.code().toString())
                } else {
                    view?.showToast(response.code().toString())
                }
            }
        })

    }

    fun getHistoryDashboard() {

        view?.showProgressBarHistory()

        Api.retrofitService.getHistoryDashboard(App.prefs.authTokenSave).enqueue(object : Callback<HistoryListData> {
            override fun onFailure(call: Call<HistoryListData>, t: Throwable) {
                view?.hideProgressBarHistory()
                view?.showToast("Tidak dapat terhubung ke server")
            }

            override fun onResponse(call: Call<HistoryListData>, response: Response<HistoryListData>) {
                if (response.isSuccessful) {
                    val historyResponse = response.body()!!
                    val historyList = historyResponse.results

                    view?.hideProgressBarHistory()
                    view?.showHistory(historyList)

                } else if (response.code() == 401) {
                    view?.hideProgressBarHistory()
                    App.prefs.authTokenSave = ""
                    view?.showToast(response.code().toString())

                } else {
                    view?.hideProgressBarHistory()
                    view?.showToast(response.code().toString())
                }
            }
        })
    }

    fun onDestroy() {
        view = null
    }
}