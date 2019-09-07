package com.meretas.itinventory.stock_inv.stock_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.meretas.itinventory.data.StockListData
import com.meretas.itinventory.services.Api
import com.meretas.itinventory.utils.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StocklistViewModel : ViewModel() {

    //STOCK Data untuk RecyclerView
    private val _stockData: MutableLiveData<StockListData> = MutableLiveData()
    val stockData: LiveData<StockListData>
        get() = _stockData

    //LOADING
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    //DATA ERROR
    private val _isError = MutableLiveData<String>()
    val isError: LiveData<String>
        get() = _isError

    //INISIALISASI
    init {
        _isLoading.value = false
        _isError.value = ""
    }

    fun getStockData(
        branch: String,
        category: String,
        active: Boolean
    ) {
        _isError.value = ""
        _isLoading.value = true
        Api.retrofitService
            .getStockList(
                token = App.prefs.authTokenSave,
                branch = branch,
                category = category,
                active = active
            )
            .enqueue(object : Callback<StockListData> {
                override fun onResponse(
                    call: Call<StockListData>,
                    response: Response<StockListData>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        //val listStock = result?.results
                        _stockData.postValue(result)
                        _isLoading.value = false
                    } else {
                        _isError.value = "Terjadi Kesalahan"
                        _isLoading.value = false
                    }
                }

                override fun onFailure(call: Call<StockListData>, t: Throwable) {
                    _isError.value = "Tidak Terkoneksi Internet"
                    _isLoading.value = false
                }
            })
    }
}