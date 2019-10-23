package com.meretas.itinventory.inv_printer.printer_edit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.meretas.itinventory.data.PrinterListData
import com.meretas.itinventory.services.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditPrinterViewModel(
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

    init {
        _isLoading.value = false
        _isSuccess.value = false
        _isError.value = ""
    }

    fun putPrinter(token: String, printer: PrinterListData.Result) {

        _isLoading.value = true
        _isError.value = ""

        apiService.putPrinter(
            token = token,
            id = printer.id,
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
                } else {
                    _isError.value = "Printer gagal dirubah"
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