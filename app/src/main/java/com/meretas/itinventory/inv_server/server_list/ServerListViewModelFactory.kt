package com.meretas.itinventory.inv_server.server_list

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.meretas.itinventory.services.ApiService

class ServerListViewModelFactory(
    private val apiService: ApiService,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("uncheked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ServerListViewModel::class.java)) {
            return ServerListViewModel(
                apiService,
                application
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}