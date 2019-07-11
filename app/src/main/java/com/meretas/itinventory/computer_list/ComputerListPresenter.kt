package com.meretas.itinventory.computer_list

import com.meretas.itinventory.data.ComputerListData
import com.meretas.itinventory.services.Api
import com.meretas.itinventory.utils.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ComputerListPresenter(private var view: ComputerListView?){

    fun getComputerData(branch:String,
                        location:String) {

        view?.showLoading()

        Api.retrofitService.getComputerList(App.prefs.authTokenSave,branch,location).enqueue(object : Callback<ComputerListData> {
            override fun onFailure(call: Call<ComputerListData>, t: Throwable) {
                view?.hideLoading()
                view?.showToast("Tidak dapat terhubung ke server")
            }

            override fun onResponse(call: Call<ComputerListData>, response: Response<ComputerListData>) {
                if (response.isSuccessful) {
                    val historyResponse = response.body()!!
                    val historyList = historyResponse.results
                    val unitCount = historyResponse.count.toString()

                    view?.hideLoading()
                    view?.showComputers(historyList)
                    view?.updateUnit(unitCount)

                } else if (response.code() == 401) {
                    view?.hideLoading()
                    App.prefs.authTokenSave = ""
                    view?.showToast(response.code().toString())
                } else {
                    view?.hideLoading()
                    view?.showToast(response.code().toString())
                }
            }
        })
    }

    fun getComputerDataSearch(search: String){

        view?.showLoading()

        Api.retrofitService.getComputerListSearch(App.prefs.authTokenSave,search).enqueue(object : Callback<ComputerListData> {
            override fun onFailure(call: Call<ComputerListData>, t: Throwable) {
                view?.hideLoading()
                view?.showToast(t.toString())
            }

            override fun onResponse(call: Call<ComputerListData>, response: Response<ComputerListData>) {
                if (response.isSuccessful) {
                    val historyResponse = response.body()!!
                    val historyList = historyResponse.results
                    val unitCount = historyResponse.count.toString()

                    view?.hideLoading()
                    view?.showComputers(historyList)
                    view?.updateUnit(unitCount)

                } else if (response.code() == 401) {
                    view?.hideLoading()
                    view?.showToast(response.code().toString())
                } else {
                    view?.hideLoading()
                    view?.showToast(response.code().toString())
                }
            }
        })
    }

    fun onDestroy() {
        view = null
    }
}