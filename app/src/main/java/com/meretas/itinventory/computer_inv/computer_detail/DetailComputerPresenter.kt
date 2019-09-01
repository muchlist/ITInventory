package com.meretas.itinventory.computer_inv.computer_detail

import com.meretas.itinventory.data.HistoryListData
import com.meretas.itinventory.services.Api
import com.meretas.itinventory.utils.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailComputerPresenter(private var view: DetailComputerView?) {

    fun getHistoryDetail(id: Int) {

        view?.showLoadingHistory()

        Api.retrofitService.getHistoryPerComputer(App.prefs.authTokenSave,id).enqueue(object : Callback<HistoryListData> {
            override fun onFailure(call: Call<HistoryListData>, t: Throwable) {
                view?.hideLoadingHistory()
                view?.showToastError("Server Sleep")
            }

            override fun onResponse(call: Call<HistoryListData>, response: Response<HistoryListData>) {
                when {
                    response.isSuccessful -> {
                        val historyResponse = response.body()!!
                        val historyList = historyResponse.results

                        view?.hideLoadingHistory()
                        view?.showHistoryList(historyList)

                    }
                    response.code() == 401 -> {
                        view?.hideLoadingHistory()
                        view?.showToastError(response.code().toString())
                    }
                    else -> {
                        view?.hideLoadingHistory()
                        view?.showToastError(response.code().toString())
                    }
                }
            }
        })
    }


    fun onDestroy() {
        view = null
    }
}