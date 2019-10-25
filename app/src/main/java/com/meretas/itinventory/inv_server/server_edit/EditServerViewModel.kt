package com.meretas.itinventory.inv_server.server_edit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.meretas.itinventory.data.ServerListData
import com.meretas.itinventory.services.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditServerViewModel(
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

    fun putServer(token: String, server: ServerListData.Result) {

        _isLoading.value = true
        _isError.value = ""

        apiService.putServer(
            token = token,
            id = server.id,
            serverName = server.serverName,
            ipAddress = server.ipAddress,
            category = server.category,
            location = server.location,
            year = server.year,
            merkModel = server.merkModel,
            status = server.status,
            note = server.note
        ).enqueue(object : Callback<ServerListData.Result> {

            override fun onResponse(
                call: Call<ServerListData.Result>,
                response: Response<ServerListData.Result>
            ) {
                if (response.isSuccessful) {
                    _isSuccess.value = true
                    _isLoading.value = false
                } else {
                    _isError.value = "Server gagal dirubah"
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<ServerListData.Result>, t: Throwable) {
                _isSuccess.value = false
                _isLoading.value = false
                _isError.value = t.toString()
            }
        })
    }
}