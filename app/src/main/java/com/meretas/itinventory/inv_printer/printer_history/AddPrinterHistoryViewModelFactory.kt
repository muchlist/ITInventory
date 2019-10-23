package com.meretas.itinventory.inv_printer.printer_history

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.meretas.itinventory.services.ApiService

class AddPrinterHistoryViewModelfactory(
    private val apiService: ApiService,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("uncheked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddPrinterHistoryViewModel::class.java)) {
            return AddPrinterHistoryViewModel(apiService, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}