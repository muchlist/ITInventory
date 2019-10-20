package com.meretas.itinventory.inv_computer.add_history

import com.meretas.itinventory.data.HistoryListComputerData
import com.meretas.itinventory.services.Api
import com.meretas.itinventory.utils.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryAddPresenter(private var view: HistoryAddView?) {

    fun postHistory(
        id: Int,
        note: String,
        status: String
    ) {
        view?.showLoading()
        Api.retrofitService.postHistory(App.prefs.authTokenSave, id, note, status)
            .enqueue(object : Callback<HistoryListComputerData.Result> {
                override fun onFailure(call: Call<HistoryListComputerData.Result>, t: Throwable) {
                    view?.showError(t.toString())
                    view?.hideLoading()
                }

                override fun onResponse(
                    call: Call<HistoryListComputerData.Result>,
                    response: Response<HistoryListComputerData.Result>
                ) {
                    when {
                        response.isSuccessful -> {
                            val okResponse = "History berhasil ditambahkan"
                            view?.showError(okResponse)
                            view?.showResult()
                            view?.hideLoading()
                        }
                        response.code() == 401 -> {
                            view?.showError(response.code().toString())
                            view?.hideLoading()
                        }
                        else -> {
                            view?.showError(response.code().toString())
                            view?.hideLoading()
                        }
                    }
                }
            })
    }

    fun onDestroy() {
        view = null
    }
}