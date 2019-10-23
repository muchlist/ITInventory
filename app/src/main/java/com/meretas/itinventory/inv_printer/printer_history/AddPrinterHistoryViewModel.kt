package com.meretas.itinventory.inv_printer.printer_history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.meretas.itinventory.data.HistoryListPrinterData
import com.meretas.itinventory.services.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddPrinterHistoryViewModel(
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

    fun postHistory(
        token: String,
        id: Int,
        note: String,
        status: String
    ) {
        val textSuccess = "History berhasil ditambahkan"
        val textUnSuccess = "History gagal ditambahkan"
        _isLoading.value = true
        _isError.value = ""
        apiService.postHistoryPrinter(token, id, note, status)
            .enqueue(object : Callback<HistoryListPrinterData.Result> {
                override fun onFailure(call: Call<HistoryListPrinterData.Result>, t: Throwable) {
                    _isError.value = textUnSuccess
                    _isLoading.value = false
                }

                override fun onResponse(
                    call: Call<HistoryListPrinterData.Result>,
                    response: Response<HistoryListPrinterData.Result>
                ) {
                    when {
                        response.isSuccessful -> {
                            _isError.value = textSuccess
                            _isSuccess.value = true //Finish()
                            _isLoading.value = false
                        }
                        response.code() == 401 -> {
                            _isError.value = textUnSuccess //seharusnya hapus credensial
                            _isLoading.value = false
                        }
                        else -> {
                            _isError.value = textUnSuccess
                            _isLoading.value = false
                        }
                    }
                }
            })
    }
}