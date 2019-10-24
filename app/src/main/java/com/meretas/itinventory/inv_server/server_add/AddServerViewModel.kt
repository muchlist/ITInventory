package com.meretas.itinventory.inv_server.server_add

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.meretas.itinventory.data.ServerListData
import com.meretas.itinventory.data.ServerPostData
import com.meretas.itinventory.services.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddServerViewModel(
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

    fun postServer(server: ServerPostData, isContinue: Boolean) {

        _isLoading.value = true
        _isError.value = ""

        apiService.postServer(
            token = server.token,
            serverName = server.serverName,
            ipAddress = server.ipAddress,
            category = server.category,
            year = server.year,
            location = server.location,
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
                    _isCont.value = isContinue
                } else {
                    _isError.value = response.code().toString()
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