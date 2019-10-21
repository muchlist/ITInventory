package com.meretas.itinventory.dashboard

import com.meretas.itinventory.data.CurrentUserData
import com.meretas.itinventory.data.HistoryListGeneralData
import com.meretas.itinventory.services.ApiService
import com.meretas.itinventory.utils.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DashboardPresenter(private var view: DashboarView?, private val apiService: ApiService) {

    fun getCurrentUserInfo(token: String) {
        apiService.getCurrentUser(token)
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

    fun getComputerHistoryDashboard(token: String) {

        view?.showProgressBarHistory()

        apiService.getHistoryDashboard(token)
            .enqueue(object : Callback<HistoryListGeneralData> {
                override fun onFailure(call: Call<HistoryListGeneralData>, t: Throwable) {
                    view?.hideProgressBarHistory()
                    view?.showToastAndReload("Tidak dapat terhubung ke server")
                }

                override fun onResponse(
                    call: Call<HistoryListGeneralData>,
                    response: Response<HistoryListGeneralData>
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