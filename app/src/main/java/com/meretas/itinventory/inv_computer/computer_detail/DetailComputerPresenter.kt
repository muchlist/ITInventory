package com.meretas.itinventory.inv_computer.computer_detail

import com.meretas.itinventory.data.HistoryListComputerData
import com.meretas.itinventory.services.Api
import com.meretas.itinventory.utils.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailComputerPresenter(private var view: DetailComputerView?) {

    fun getHistoryDetail(id: Int) {

        view?.showLoadingHistory()

        Api.retrofitService.getHistoryPerComputer(App.prefs.authTokenSave, id)
            .enqueue(object : Callback<HistoryListComputerData> {
                override fun onFailure(call: Call<HistoryListComputerData>, t: Throwable) {
                    view?.hideLoadingHistory()
                    view?.showToastError("Server Sleep")
                }

                override fun onResponse(
                    call: Call<HistoryListComputerData>,
                    response: Response<HistoryListComputerData>
                ) {
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