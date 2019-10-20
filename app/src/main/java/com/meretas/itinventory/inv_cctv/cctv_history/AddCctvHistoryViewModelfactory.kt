package com.meretas.itinventory.inv_cctv.cctv_history

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.meretas.itinventory.services.ApiService

class AddCctvHistoryViewModelfactory(
    private val apiService: ApiService,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("uncheked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddCctvHistoryViewModel::class.java)) {
            return AddCctvHistoryViewModel(apiService, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}