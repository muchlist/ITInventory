package com.meretas.itinventory.inv_printer.printer_add

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.meretas.itinventory.data.PrinterListData
import com.meretas.itinventory.data.PrinterPostData
import com.meretas.itinventory.services.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddPrinterViewModel(
    private val apiService: ApiService,
    application: Application
) : AndroidViewModel(application) {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean>
        get() = _isSuccess

    private val _isError = MutableLiveData<String>()
    val isError: LiveData<String>
        get() = _isError

    private val _isCont = MutableLiveData<Boolean>()
    val isCont: LiveData<Boolean>
        get() = _isCont

    init {
        _isLoading.value = false
        _isSuccess.value = false
        _isError.value = ""
        _isCont.value = true
    }

    fun postPrinter(printer: PrinterPostData, isContinue: Boolean) {

        _isLoading.value = true
        _isError.value = ""

        apiService.postPrinter(
            token = printer.token,
            printerName = printer.printerName,
            ipAddress = printer.ipAddress,
            division = printer.division,
            year = printer.year,
            merkModel = printer.merkModel,
            status = printer.status,
            note = printer.note
        ).enqueue(object : Callback<PrinterListData.Result> {

            override fun onResponse(
                call: Call<PrinterListData.Result>,
                response: Response<PrinterListData.Result>
            ) {
                if (response.isSuccessful) {
                    _isSuccess.value = true
                    _isLoading.value = false
                    _isCont.value = isContinue
                } else {
                    _isError.value = response.code().toString()
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<PrinterListData.Result>, t: Throwable) {
                _isSuccess.value = false
                _isLoading.value = false
                _isError.value = t.toString()
            }
        })
    }
}