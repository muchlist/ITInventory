package com.meretas.itinventory.inv_printer.printer_detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.meretas.itinventory.data.HistoryListGeneralData
import com.meretas.itinventory.data.HistoryListPrinterData
import com.meretas.itinventory.data.PrinterListData
import com.meretas.itinventory.services.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailPrinterViewModel(
    private val apiService: ApiService,
    application: Application
) : AndroidViewModel(application) {

    //INTENT DATA > Stock > Refresh jika ada perubahan
    private val _printerDetailData: MutableLiveData<PrinterListData.Result> = MutableLiveData()
    val printerDetailData: LiveData<PrinterListData.Result>
        get() = _printerDetailData

    //LOADING
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    //Toast
    private val _isError = MutableLiveData<String>()
    val isError: LiveData<String>
        get() = _isError

    //History RV Data
    private val _printerDetailHistoryPrinterData: MutableLiveData<List<HistoryListGeneralData.Result>> =
        MutableLiveData()
    val printerDetailHistoryPrinterData: LiveData<List<HistoryListGeneralData.Result>>
        get() = _printerDetailHistoryPrinterData


    //INITIATE
    init {
        _printerDetailData.value = PrinterListData.Result(
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
    fun injectPrinterDataToViewModel(data: PrinterListData.Result) {
        _printerDetailData.value = data
    }

    fun getPrinterHistory(token: String, printerId: Int) {
        _isError.value = ""
        _isLoading.value = true
        apiService.getHistoryPerPrinter(token, printerId)
            .enqueue(object : Callback<HistoryListPrinterData> {

                override fun onResponse(
                    call: Call<HistoryListPrinterData>,
                    response: Response<HistoryListPrinterData>
                ) {
                    when {
                        response.isSuccessful -> {
                            val data = response.body()?.results
                            val historyData: MutableList<HistoryListGeneralData.Result> =
                                mutableListOf()
                            data?.let {
                                for (i in data) {
                                    historyData.add(
                                        HistoryListGeneralData.Result(
                                            id = i.id,
                                            author = i.author,
                                            branch = i.branch,
                                            computer = i.printer,
                                            computerId = i.printerId,
                                            createdAt = i.createdAt,
                                            updatedAt = i.updatedAt,
                                            statusHistory = i.statusHistory,
                                            note = i.note
                                        )
                                    )
                                }
                            }
                            _printerDetailHistoryPrinterData.postValue(historyData)
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

                override fun onFailure(call: Call<HistoryListPrinterData>, t: Throwable) {
                    _isLoading.value = false
                    _isError.value = "Data yang ditampilkan gagal diperbaharui"
                }
            })
    }

    //REFRESH STOCK DATA DARI INTENT JIKA ADA PERUBAHAN isStockChangePlus
    fun getPrinterRefresh(token: String, id: Int) {
        _isError.value = ""
        apiService.getPrinterRefresh(token, id)
            .enqueue(object : Callback<PrinterListData.Result> {
                override fun onResponse(
                    call: Call<PrinterListData.Result>,
                    response: Response<PrinterListData.Result>
                ) {
                    if (response.isSuccessful) {
                        _printerDetailData.postValue(response.body())
                    } else {
                        _isError.value = "Data yang ditampilkan gagal diperbaharui"
                    }
                }

                override fun onFailure(call: Call<PrinterListData.Result>, t: Throwable) {
                    _isError.value = "Data yang ditampilkan gagal diperbaharui"
                }
            })
    }


}