package com.meretas.itinventory.inv_printer.printer_detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.meretas.itinventory.services.ApiService

class DetailPrinterViewModelFactory(
    private val apiService: ApiService,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("uncheked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailPrinterViewModel::class.java)) {
            return DetailPrinterViewModel(apiService, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}