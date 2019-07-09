package com.meretas.itinventory.add_history

import com.meretas.itinventory.data.HistoryListData
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
    ) { view?.showLoading()
        Api.retrofitService.postHistory(App.prefs.authTokenSave, id, note, status)
            .enqueue(object : Callback<HistoryListData.Result> {
                override fun onFailure(call: Call<HistoryListData.Result>, t: Throwable) {
                    // view?.updateConnection(false)
                    view?.hideLoading()
                }

                override fun onResponse(
                    call: Call<HistoryListData.Result>,
                    response: Response<HistoryListData.Result>
                ) {
                    if (response.isSuccessful) {
                        val okResponse = "History berhasil ditambahkan"
                        view?.showError(okResponse)
                        view?.showResult()
                        view?.hideLoading()
                    } else if (response.code() == 401) {
                        //  view?.showToast(App.prefs.authTokenSave)
                        view?.hideLoading()
                    } else {
                        view?.showError(response.code().toString())
                        view?.hideLoading()
                    }
                }
            })
    }

    fun onDestroy() {
        view = null
    }
}