package com.meretas.itinventory.inv_server.server_detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.meretas.itinventory.data.HistoryListGeneralData
import com.meretas.itinventory.data.HistoryListServerData
import com.meretas.itinventory.data.ServerListData
import com.meretas.itinventory.services.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailServerViewModel(
    private val apiService: ApiService,
    application: Application
) : AndroidViewModel(application) {

    //INTENT DATA > Stock > Refresh jika ada perubahan
    private val _serverDetailData: MutableLiveData<ServerListData.Result> = MutableLiveData()
    val serverDetailData: LiveData<ServerListData.Result>
        get() = _serverDetailData

    //LOADING
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    //Toast
    private val _isError = MutableLiveData<String>()
    val isError: LiveData<String>
        get() = _isError

    //History RV Data
    private val _serverDetailHistoryServerData: MutableLiveData<List<HistoryListGeneralData.Result>> =
        MutableLiveData()
    val serverDetailHistoryServerData: LiveData<List<HistoryListGeneralData.Result>>
        get() = _serverDetailHistoryServerData


    //INITIATE
    init {
        _serverDetailData.value = ServerListData.Result(
            "null",
            "null",
            "null",
            "null",
            0,
            "null",
            "null",
            "null",
            "null",
            "null",
            "null",
            "null"
        )
    }

    //Mengambil data dari activity intent ke live data
    fun injectServerDataToViewModel(data: ServerListData.Result) {
        _serverDetailData.value = data
    }

    fun getServerHistory(token: String, serverId: Int) {
        _isError.value = ""
        _isLoading.value = true
        apiService.getHistoryPerServer(token, serverId)
            .enqueue(object : Callback<HistoryListServerData> {

                override fun onResponse(
                    call: Call<HistoryListServerData>,
                    response: Response<HistoryListServerData>
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
                                            computer = i.server,
                                            computerId = i.serverId,
                                            createdAt = i.createdAt,
                                            updatedAt = i.updatedAt,
                                            statusHistory = i.statusHistory,
                                            note = i.note
                                        )
                                    )
                                }
                            }
                            _serverDetailHistoryServerData.postValue(historyData)
                            _isLoading.value = false
                        }
                        response.code() == 401 -> {
                            //harus logout
                            _isError.value = "Data yang ditampilkan gagal diperbaharui"
                            _isLoading.value = false
                        }
                        else -> {
                            _isError.value = "Data yang ditampilkan gagal diperbaharui"
                            _isLoading.value = false
                        }
                    }
                }

                override fun onFailure(call: Call<HistoryListServerData>, t: Throwable) {
                    _isLoading.value = false
                    _isError.value = "Data yang ditampilkan gagal diperbaharui"
                }
            })
    }

    //REFRESH STOCK DATA DARI INTENT JIKA ADA PERUBAHAN isStockChangePlus
    fun getServerRefresh(token: String, id: Int) {
        _isError.value = ""
        apiService.getServerRefresh(token, id)
            .enqueue(object : Callback<ServerListData.Result> {
                override fun onResponse(
                    call: Call<ServerListData.Result>,
                    response: Response<ServerListData.Result>
                ) {
                    if (response.isSuccessful) {
                        _serverDetailData.postValue(response.body())
                    } else {
                        _isError.value = "Data yang ditampilkan gagal diperbaharui"
                    }
                }

                override fun onFailure(call: Call<ServerListData.Result>, t: Throwable) {
                    _isError.value = "Data yang ditampilkan gagal diperbaharui"
                }
            })
    }


}