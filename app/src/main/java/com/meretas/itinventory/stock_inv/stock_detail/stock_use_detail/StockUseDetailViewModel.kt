package com.meretas.itinventory.stock_inv.stock_detail.stock_use_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.meretas.itinventory.R
import com.meretas.itinventory.data.AddAndConsumeData
import com.meretas.itinventory.services.Api
import com.meretas.itinventory.utils.App
import com.meretas.itinventory.utils.FROM_ADDITION_STOCK
import com.meretas.itinventory.utils.Statis
import com.meretas.itinventory.utils.Statis.Companion.isStockChangeMinus
import com.meretas.itinventory.utils.Statis.Companion.isStockChangePlus
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StockUseDetailViewModel : ViewModel() {

    //INTENT DATA > Stock Use > Refresh jika ada perubahan
    private val _stockUseDetailData: MutableLiveData<AddAndConsumeData.Result> = MutableLiveData()
    val stockUseDetailData: LiveData<AddAndConsumeData.Result>
        get() = _stockUseDetailData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _isDeleteSuccess = MutableLiveData<Boolean>()
    val isDeleteSuccess: LiveData<Boolean>
        get() = _isDeleteSuccess

    private val _isError = MutableLiveData<String>()
    val isError: LiveData<String>
        get() = _isError

    init {
        _isLoading.value = false
        _isDeleteSuccess.value = false
        _isError.value = ""
        _stockUseDetailData.value = AddAndConsumeData.Result(
            "null",
            "null",
            "null",
            "null",
            "null",
            0,
            "null",
            0,
            "null",
            0,
            "null",
            "null"
        )

    }

    //Mengambil data dari activity intent ke live data
    fun postDetailStockUse(data: AddAndConsumeData.Result) {
        _stockUseDetailData.value = data
    }

    fun getStockUseDetailRefreshFromAddition(id:Int){
        _isError.value = ""

        Api.retrofitService.getAdditionDetail(
            App.prefs.authTokenSave, id
        ).enqueue(object : Callback<AddAndConsumeData.Result> {

            override fun onResponse(call: Call<AddAndConsumeData.Result>, response: Response<AddAndConsumeData.Result>) {
                if (response.isSuccessful) {
                    _isError.value = "Data diperbarui"
                    _stockUseDetailData.value = response.body()
                    Statis.isStockUseDetailUpdate = false
                    isStockChangePlus = true
                } else {
                    _isError.value = response.code().toString()
                }
            }

            override fun onFailure(call: Call<AddAndConsumeData.Result>, t: Throwable) {
                _isError.value = R.string.error.toString()
            }
        })
    }

    fun getStockUseDetailRefreshFromConsume(id:Int){
        _isError.value = ""

        Api.retrofitService.getConsumeDetail(
            App.prefs.authTokenSave, id
        ).enqueue(object : Callback<AddAndConsumeData.Result> {

            override fun onResponse(call: Call<AddAndConsumeData.Result>, response: Response<AddAndConsumeData.Result>) {
                if (response.isSuccessful) {
                    _isError.value = "Data diperbarui"
                    _stockUseDetailData.value = response.body()
                    Statis.isStockUseDetailUpdate = false
                    isStockChangeMinus = true
                } else {
                    _isError.value = response.code().toString()
                }
            }

            override fun onFailure(call: Call<AddAndConsumeData.Result>, t: Throwable) {
                _isError.value = R.string.error.toString()
            }
        })
    }


    //DELETE BUTTON START
    fun deleteAddition(additionsID: Int) {

        _isLoading.value = true
        _isError.value = ""

        Api.retrofitService.deleteAddition(
            App.prefs.authTokenSave, additionsID
        ).enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    _isDeleteSuccess.value = true
                    _isLoading.value = false
                } else {
                    _isError.value = response.code().toString()
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                _isDeleteSuccess.value = false
                _isLoading.value = false
                _isError.value = R.string.error.toString()
            }
        })
    }

    fun deleteConsume(additionsID: Int) {

        _isLoading.value = true
        _isError.value = ""

        Api.retrofitService.deleteConsume(
            App.prefs.authTokenSave, additionsID
        ).enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    _isDeleteSuccess.value = true
                    _isLoading.value = false
                } else {
                    _isError.value = response.code().toString()
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                _isDeleteSuccess.value = false
                _isLoading.value = false
                _isError.value = R.string.error.toString()
            }
        })
    }
    //DELETE BUTTON END
}