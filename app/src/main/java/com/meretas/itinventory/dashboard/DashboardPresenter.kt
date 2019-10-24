package com.meretas.itinventory.dashboard

import com.meretas.itinventory.data.CurrentUserData
import com.meretas.itinventory.data.HistoryListCctvData
import com.meretas.itinventory.data.HistoryListGeneralData
import com.meretas.itinventory.data.HistoryListPrinterData
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

    fun getPrinterHistoryDashboard(token: String) {

        view?.showProgressBarHistory()

        apiService.getHistoryPrinterDashboard(token)
            .enqueue(object : Callback<HistoryListPrinterData> {
                override fun onFailure(call: Call<HistoryListPrinterData>, t: Throwable) {
                    view?.hideProgressBarHistory()
                    view?.showToastAndReload("Tidak dapat terhubung ke server")
                }

                override fun onResponse(
                    call: Call<HistoryListPrinterData>,
                    response: Response<HistoryListPrinterData>
                ) {
                    when {
                        response.isSuccessful -> {
                            val data = response.body()?.results
                            val historyData: MutableList<HistoryListGeneralData.Result> =
                                mutableListOf()
                            data?.let {
                                for (i in data) {
                                    historyData.add(
                                        HistoryListGeneralData.Result(
                                            id = i.id,
                                            author = i.author,
                                            branch = i.branch,
                                            computer = i.printer + " (print)",
                                            computerId = i.printerId,
                                            createdAt = i.createdAt,
                                            updatedAt = i.updatedAt,
                                            statusHistory = i.statusHistory,
                                            note = i.note
                                        )
                                    )
                                }
                            }

                            view?.hideProgressBarHistory()
                            view?.showHistory(historyData)
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

    fun getCctvHistoryDashboard(token: String) {

        view?.showProgressBarHistory()

        apiService.getHistoryCctvDashboard(token)
            .enqueue(object : Callback<HistoryListCctvData> {
                override fun onFailure(call: Call<HistoryListCctvData>, t: Throwable) {
                    view?.hideProgressBarHistory()
                    view?.showToastAndReload("Tidak dapat terhubung ke server")
                }

                override fun onResponse(
                    call: Call<HistoryListCctvData>,
                    response: Response<HistoryListCctvData>
                ) {
                    when {
                        response.isSuccessful -> {
                            val data = response.body()?.results
                            val historyData: MutableList<HistoryListGeneralData.Result> =
                                mutableListOf()
                            data?.let {
                                for (i in data) {
                                    historyData.add(
                                        HistoryListGeneralData.Result(
                                            id = i.id,
                                            author = i.author,
                                            branch = i.branch,
                                            computer = i.cctv + " (cctv)",
                                            computerId = i.cctvId,
                                            createdAt = i.createdAt,
                                            updatedAt = i.updatedAt,
                                            statusHistory = i.statusHistory,
                                            note = i.note
                                        )
                                    )
                                }
                            }

                            view?.hideProgressBarHistory()
                            view?.showHistory(historyData)
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