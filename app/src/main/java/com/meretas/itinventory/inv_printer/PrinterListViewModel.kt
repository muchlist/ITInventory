package com.meretas.itinventory.inv_printer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.meretas.itinventory.data.PrinterListData
import com.meretas.itinventory.services.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PrinterListViewModel(
    private val apiService: ApiService,
    application: Application
) : AndroidViewModel(application) {

    //STOCK Data untuk RecyclerView
    private val _printerData: MutableLiveData<PrinterListData> = MutableLiveData()
    /*val printerData: LiveData<CctvListData>
        get() = _printerData*/

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

    fun requestPrinterData(
        token: String,
        branch: String,
        division: String,
        status: String
    ) {
        _isError.value = ""
        _isLoading.value = true
        apiService.getPrinterList(
            token = token, //App.prefs.authTokenSave,
            branch = branch,
            division = division,
            status = status
        )
            .enqueue(object : Callback<PrinterListData> {
                override fun onResponse(
                    call: Call<PrinterListData>,
                    response: Response<PrinterListData>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        _printerData.postValue(result)
                        _isLoading.value = false
                    } else {
                        _isError.value = "Terjadi Kesalahan ${response.code()}"
                        _isLoading.value = false
                    }
                }

                override fun onFailure(call: Call<PrinterListData>, t: Throwable) {
                    _isError.value = "Tidak Terkoneksi Internet"
                    _isLoading.value = false
                }
            })
    }

    fun requestPrinterDataSearch(
        token: String,
        search: String
    ) {
        _isError.value = ""
        _isLoading.value = true
        apiService.getPrinterListSearch(
            token = token,
            search = search
        )
            .enqueue(object : Callback<PrinterListData> {
                override fun onResponse(
                    call: Call<PrinterListData>,
                    response: Response<PrinterListData>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        _printerData.postValue(result)
                        _isLoading.value = false
                    } else {
                        _isError.value = "Terjadi Kesalahan"
                        _isLoading.value = false
                    }
                }

                override fun onFailure(call: Call<PrinterListData>, t: Throwable) {
                    _isError.value = "Tidak Terkoneksi Internet"
                    _isLoading.value = false
                }
            })
    }

    fun getPrinterData(): MutableLiveData<PrinterListData> {
        return _printerData
    }
}