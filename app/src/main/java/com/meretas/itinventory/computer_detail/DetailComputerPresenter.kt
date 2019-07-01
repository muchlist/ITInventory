package com.meretas.itinventory.computer_detail

import com.meretas.itinventory.data.HistoryListData
import com.meretas.itinventory.services.Api
import com.meretas.itinventory.utils.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailComputerPresenter(private var view: DetailComputerView?) {


    //History Harus dimodifikasi menggunakan retrofit berbeda
    fun getHistoryDetail() {

        view?.showLoadingHistory()

        Api.retrofitService.getHistoryDashboard(App.prefs.authToken).enqueue(object : Callback<HistoryListData> {
            override fun onFailure(call: Call<HistoryListData>, t: Throwable) {
                view?.hideLoadingHistory()
                view?.showToastError(t.toString())
                //GAGAL LOAD
            }

            override fun onResponse(call: Call<HistoryListData>, response: Response<HistoryListData>) {
                if (response.isSuccessful) {
                    val historyResponse = response.body()!!
                    val historyList = historyResponse.results

                    view?.hideLoadingHistory()
                    view?.showHistoryList(historyList)

                } else if (response.code() == 401) {
                    view?.hideLoadingHistory()
                    view?.showToastError(response.code().toString())
                    //APABILA TOKEN SALAH LEMPAR KE LOGIN

                } else {
                    view?.hideLoadingHistory()
                    view?.showToastError(response.code().toString())
                    //GAGAL LOAD
                }
            }
        })
    }


    fun onDestroy() {
        view = null
    }
}