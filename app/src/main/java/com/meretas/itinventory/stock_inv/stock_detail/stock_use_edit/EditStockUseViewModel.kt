package com.meretas.itinventory.stock_inv.stock_detail.stock_use_edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.meretas.itinventory.data.AddAndConsumeData
import com.meretas.itinventory.data.AddAndConsumePostData
import com.meretas.itinventory.services.Api
import com.meretas.itinventory.utils.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditStockUseViewModel : ViewModel() {

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

    fun editStockAddition(stockUseData: AddAndConsumePostData, id: Int) {

        _isLoading.value = true
        _isError.value = ""

        Api.retrofitService.putAddition(
            App.prefs.authTokenSave,
            id,
            stockUseData.eventNumber,
            stockUseData.note,
            stockUseData.qty
        ).enqueue(object : Callback<AddAndConsumeData.Result> {

            override fun onResponse(
                call: Call<AddAndConsumeData.Result>,
                response: Response<AddAndConsumeData.Result>
            ) {
                if (response.isSuccessful) {
                    _isSuccess.value = true
                    _isLoading.value = false
                } else {
                    _isError.value = response.code().toString()
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<AddAndConsumeData.Result>, t: Throwable) {
                _isSuccess.value = false
                _isLoading.value = false
                _isError.value = t.toString()
            }
        })
    }

    fun editStockConsume(stockUseData: AddAndConsumePostData, id: Int) {

        _isLoading.value = true
        _isError.value = ""

        Api.retrofitService.putConsume(
            App.prefs.authTokenSave,
            id,
            stockUseData.eventNumber,
            stockUseData.note,
            stockUseData.qty
        ).enqueue(object : Callback<AddAndConsumeData.Result> {

            override fun onResponse(
                call: Call<AddAndConsumeData.Result>,
                response: Response<AddAndConsumeData.Result>
            ) {
                if (response.isSuccessful) {
                    _isSuccess.value = true
                    _isLoading.value = false
                } else {
                    _isError.value = response.code().toString()
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<AddAndConsumeData.Result>, t: Throwable) {
                _isSuccess.value = false
                _isLoading.value = false
                _isError.value = t.toString()
            }
        })
    }

}