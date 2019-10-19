package com.meretas.itinventory.inv_stock.stock_edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.meretas.itinventory.data.StockListData
import com.meretas.itinventory.data.StockPostData
import com.meretas.itinventory.services.Api
import com.meretas.itinventory.utils.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditStockViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean>
        get() = _isSuccess

    private val _isError = MutableLiveData<String>()
    val isError: LiveData<String>
        get() = _isError

    init {
        _isLoading.value = false
        _isSuccess.value = false
        _isError.value = ""
    }

    fun postStock(stock: StockPostData) {

        _isLoading.value = true
        _isError.value = ""

        Api.retrofitService.putStock(
            App.prefs.authTokenSave,
            stock.id,
            stock.stockName,
            stock.category,
            stock.threshold,
            stock.unit,
            stock.active,
            stock.note
        ).enqueue(object : Callback<StockListData.Result> {

            override fun onResponse(
                call: Call<StockListData.Result>,
                response: Response<StockListData.Result>
            ) {
                if (response.isSuccessful) {
                    _isSuccess.value = true
                    _isLoading.value = false
                } else {
                    _isError.value = response.code().toString()
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<StockListData.Result>, t: Throwable) {
                _isSuccess.value = false
                _isLoading.value = false
                _isError.value = t.toString()
            }
        })
    }
}