package com.meretas.itinventory.inv_stock.stock_detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.meretas.itinventory.data.AddAndConsumeData
import com.meretas.itinventory.data.StockListData
import com.meretas.itinventory.services.ApiService
import com.meretas.itinventory.utils.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailStockViewModel(
    private val apiService: ApiService,
    application: Application
) : AndroidViewModel(application) {

    //INTENT DATA > Stock > Refresh jika ada perubahan
    private val _stockDetailData: MutableLiveData<StockListData.Result> = MutableLiveData()
    val stockDetailData: LiveData<StockListData.Result>
        get() = _stockDetailData

    private val _isStockDetailError = MutableLiveData<String>()
    val isStockDetailError: LiveData<String>
        get() = _isStockDetailError


    //ADDITION DATA
    private val _additionData: MutableLiveData<List<AddAndConsumeData.Result>> = MutableLiveData()
    val additionData: LiveData<List<AddAndConsumeData.Result>>
        get() = _additionData

    private val _isAdditionListLoading = MutableLiveData<Boolean>()
    val isAdditionListLoading: LiveData<Boolean>
        get() = _isAdditionListLoading

    private val _isAdditionListError = MutableLiveData<String>()
    val isAdditionListError: LiveData<String>
        get() = _isAdditionListError

    //CONSUME DATA
    private val _consumeData: MutableLiveData<List<AddAndConsumeData.Result>> = MutableLiveData()
    val consumeData: LiveData<List<AddAndConsumeData.Result>>
        get() = _consumeData

    private val _isConsumeListLoading = MutableLiveData<Boolean>()
    val isConsumeListLoading: LiveData<Boolean>
        get() = _isConsumeListLoading

    private val _isConsumeListError = MutableLiveData<String>()
    val isConsumeListError: LiveData<String>
        get() = _isConsumeListError

    //STATUS CHANGE
    private val _isStatusChangeError = MutableLiveData<String>()
    val isStatusChangeError: LiveData<String>
        get() = _isStatusChangeError

    //INITIATE
    init {
        _stockDetailData.value = StockListData.Result(
            true,
            "null",
            "null",
            "null",
            "null",
            0,
            "null",
            0,
            "null",
            0,
            0,
            "null",
            "null"
        )
    }

    //REFRESH STOCK DATA DARI INTENT JIKA ADA PERUBAHAN isStockChangePlus
    fun getStockRefresh(id: Int) {
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
                        _isStockDetailError.value = "Data yang ditampilkan gagal diperbaharui"
                    }
                }

                override fun onFailure(call: Call<StockListData.Result>, t: Throwable) {
                    _isStockDetailError.value = "Data yang ditampilkan gagal diperbaharui"
                }
            })
    }


    //Mengambil data dari activity intent ke live data
    fun postDetailStock(data: StockListData.Result) {
        _stockDetailData.value = data
    }

    //Mengambil data list addition stock
    fun getAdditionList(id: Int) {
        _isAdditionListError.value = ""
        _isAdditionListLoading.value = true
        apiService.getAdditionList(App.prefs.authTokenSave, id)
            .enqueue(object : Callback<AddAndConsumeData> {
                override fun onResponse(
                    call: Call<AddAndConsumeData>,
                    response: Response<AddAndConsumeData>
                ) {
                    if (response.isSuccessful) {
                        val listAddition = response.body()?.results
                        _additionData.postValue(listAddition)
                        _isAdditionListLoading.value = false
                    } else {
                        _isAdditionListError.value = "Terjadi Kesalahan"
                        _isAdditionListLoading.value = false
                    }
                }

                override fun onFailure(call: Call<AddAndConsumeData>, t: Throwable) {
                    _isAdditionListLoading.value = false
                }
            })
    }


    //Mengambil data list addition stock
    fun getConsumeList(id: Int) {
        _isConsumeListError.value = ""
        _isConsumeListLoading.value = true
        apiService.getConsumeList(App.prefs.authTokenSave, id)
            .enqueue(object : Callback<AddAndConsumeData> {
                override fun onResponse(
                    call: Call<AddAndConsumeData>,
                    response: Response<AddAndConsumeData>
                ) {
                    if (response.isSuccessful) {
                        val listConsume = response.body()?.results
                        _consumeData.postValue(listConsume)
                        _isConsumeListLoading.value = false
                    } else {
                        _isConsumeListError.value = "Terjadi Kesalahan"
                        _isConsumeListLoading.value = false
                    }
                }

                override fun onFailure(call: Call<AddAndConsumeData>, t: Throwable) {
                    _isConsumeListLoading.value = false
                }
            })
    }

    //Merubah status Stock
    fun changeStatus(id: Int, status: String) {
        _isStatusChangeError.value = ""
        apiService.getChangeStockStatus(App.prefs.authTokenSave, id, status)
            .enqueue(object : Callback<StockListData.Result> {
                override fun onResponse(
                    call: Call<StockListData.Result>,
                    response: Response<StockListData.Result>
                ) {
                    if (response.isSuccessful) {
                        val listAddition = response.body()
                        _stockDetailData.postValue(listAddition)
                        _isStatusChangeError.value = "Status dirubah"
                    } else {
                        _isStatusChangeError.value = "Status gagal dirubah"
                    }
                }

                override fun onFailure(call: Call<StockListData.Result>, t: Throwable) {
                    _isStatusChangeError.value = "Status gagal dirubah"
                }
            })
    }


}