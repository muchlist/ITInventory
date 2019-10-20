package com.meretas.itinventory.inv_cctv.cctv_detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.meretas.itinventory.data.CctvListData
import com.meretas.itinventory.data.HistoryListCctvData
import com.meretas.itinventory.services.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailCctvViewModel(
    private val apiService: ApiService,
    application: Application
) : AndroidViewModel(application) {

    //INTENT DATA > Stock > Refresh jika ada perubahan
    private val _cctvDetailData: MutableLiveData<CctvListData.Result> = MutableLiveData()
    val cctvDetailData: LiveData<CctvListData.Result>
        get() = _cctvDetailData

    //LOADING
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    //Toast
    private val _isError = MutableLiveData<String>()
    val isError: LiveData<String>
        get() = _isError

    //History RV Data
    private val _cctvDetailHistoryCctvData: MutableLiveData<HistoryListCctvData> = MutableLiveData()
    val cctvDetailHistoryCctvData: LiveData<HistoryListCctvData>
        get() = _cctvDetailHistoryCctvData


    //INITIATE
    init {
        _cctvDetailData.value = CctvListData.Result(
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
    fun injectCctvDataToViewModel(data: CctvListData.Result) {
        _cctvDetailData.value = data
    }

    fun getCctvHistory(token : String, cctvId: Int){
        _isError.value = ""
        _isLoading.value = true
        apiService.getHistoryPerCctv(token, cctvId)
            .enqueue(object : Callback<HistoryListCctvData> {

                override fun onResponse(
                    call: Call<HistoryListCctvData>,
                    response: Response<HistoryListCctvData>
                ) {
                    when {
                        response.isSuccessful -> {
                            val listAddition = response.body()
                            _cctvDetailHistoryCctvData.postValue(listAddition)
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

                override fun onFailure(call: Call<HistoryListCctvData>, t: Throwable) {
                    _isLoading.value = false
                    _isError.value = "Data yang ditampilkan gagal diperbaharui"
                }
            })
    }

    //REFRESH STOCK DATA DARI INTENT JIKA ADA PERUBAHAN isStockChangePlus
    /*fun getStockRefresh(id: Int) {
        apiService.getStockDetail(App.prefs.authTokenSave, id)
            .enqueue(object : Callback<StockListData.Result> {
                override fun onResponse(
                    call: Call<StockListData.Result>,
                    response: Response<StockListData.Result>
                ) {
                    if (response.isSuccessful) {
                        val listAddition = response.body()
                        _stockDetailData.postValue(listAddition)
                    } else {
                        _isError.value = "Data yang ditampilkan gagal diperbaharui"
                    }
                }

                override fun onFailure(call: Call<StockListData.Result>, t: Throwable) {
                    _isError.value = "Data yang ditampilkan gagal diperbaharui"
                }
            })
    }*/


}