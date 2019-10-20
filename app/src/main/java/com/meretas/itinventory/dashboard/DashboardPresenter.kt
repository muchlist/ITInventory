package com.meretas.itinventory.dashboard

import com.meretas.itinventory.data.CurrentUserData
import com.meretas.itinventory.data.HistoryListComputerData
import com.meretas.itinventory.services.Api
import com.meretas.itinventory.utils.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DashboardPresenter(private var view: DashboarView?) {

    fun getCurrentUserInfo() {
        Api.retrofitService.getCurrentUser(App.prefs.authTokenSave)
            .enqueue(object : Callback<CurrentUserData> {
                override fun onFailure(call: Call<CurrentUserData>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<CurrentUserData>,
                    response: Response<CurrentUserData>
                ) {
                    when {
                        response.isSuccessful -> {
                            val userResponse = response.body()!!
                            val name = userResponse.firstName + " " + userResponse.lastName
                            val branch = userResponse.profile?.userBranch ?: "Profil Null"
                            val isReadOnly = userResponse.profile?.isReadOnly ?: true

                            view?.getUserInfo(name, branch, isReadOnly)
                            App.prefs.isCompleteLogin = true
                        }
                        response.code() == 401 -> {
                            App.prefs.authTokenSave = ""
                            view?.showToastAndReload(response.code().toString())
                        }
                        else -> view?.showToastAndReload(response.code().toString())
                    }
                }
            })

    }

    fun getHistoryDashboard() {

        view?.showProgressBarHistory()

        Api.retrofitService.getHistoryDashboard(App.prefs.authTokenSave)
            .enqueue(object : Callback<HistoryListComputerData> {
                override fun onFailure(call: Call<HistoryListComputerData>, t: Throwable) {
                    view?.hideProgressBarHistory()
                    view?.showToastAndReload("Tidak dapat terhubung ke server")
                }

                override fun onResponse(
                    call: Call<HistoryListComputerData>,
                    response: Response<HistoryListComputerData>
                ) {
                    when {
                        response.isSuccessful -> {
                            val historyResponse = response.body()!!
                            val historyList = historyResponse.results

                            view?.hideProgressBarHistory()
                            view?.showHistory(historyList)

                        }
                        response.code() == 401 -> {
                            view?.hideProgressBarHistory()
                            App.prefs.authTokenSave = ""
                            view?.showToastAndReload(response.code().toString())

                        }
                        else -> {
                            view?.hideProgressBarHistory()
                            view?.showToastAndReload(response.code().toString())
                        }
                    }
                }
            })
    }

    fun onDestroy() {
        view = null
    }
}