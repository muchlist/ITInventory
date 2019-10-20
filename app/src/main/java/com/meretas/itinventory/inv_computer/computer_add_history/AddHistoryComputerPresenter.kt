package com.meretas.itinventory.inv_computer.computer_add_history

import com.meretas.itinventory.data.HistoryListGeneralData
import com.meretas.itinventory.services.Api
import com.meretas.itinventory.utils.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddHistoryComputerPresenter(private var historyComputerView: AddHistoryComputerView?) {

    fun postHistory(
        id: Int,
        note: String,
        status: String
    ) {
        historyComputerView?.showLoading()
        Api.retrofitService.postHistory(App.prefs.authTokenSave, id, note, status)
            .enqueue(object : Callback<HistoryListGeneralData.Result> {
                override fun onFailure(call: Call<HistoryListGeneralData.Result>, t: Throwable) {
                    historyComputerView?.showError(t.toString())
                    historyComputerView?.hideLoading()
                }

                override fun onResponse(
                    call: Call<HistoryListGeneralData.Result>,
                    response: Response<HistoryListGeneralData.Result>
                ) {
                    when {
                        response.isSuccessful -> {
                            val okResponse = "History berhasil ditambahkan"
                            historyComputerView?.showError(okResponse)
                            historyComputerView?.showResult()
                            historyComputerView?.hideLoading()
                        }
                        response.code() == 401 -> {
                            historyComputerView?.showError(response.code().toString())
                            historyComputerView?.hideLoading()
                        }
                        else -> {
                            historyComputerView?.showError(response.code().toString())
                            historyComputerView?.hideLoading()
                        }
                    }
                }
            })
    }

    fun onDestroy() {
        historyComputerView = null
    }
}