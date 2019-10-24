package com.meretas.itinventory.inv_server.server_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.meretas.itinventory.data.ServerListData
import com.meretas.itinventory.services.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServerListViewModel(
    private val apiService: ApiService,
    application: Application
) : AndroidViewModel(application) {

    //STOCK Data untuk RecyclerView
    private val _serverData: MutableLiveData<ServerListData> = MutableLiveData()
    /*val serverData: LiveData<CctvListData>
        get() = _serverData*/

    //LOADING
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    //DATA ERROR
    private val _isError = MutableLiveData<String>()
    val isError: LiveData<String>
        get() = _isError

    //INISIALISASI
    init {
        _isLoading.value = false
        _isError.value = ""
    }

    fun requestServerData(
        token: String,
        branch: String,
        category: String,
        status: String
    ) {
        _isError.value = ""
        _isLoading.value = true
        apiService.getServerList(
            token = token, //App.prefs.authTokenSave,
            branch = branch,
            category = category,
            status = status
        )
            .enqueue(object : Callback<ServerListData> {
                override fun onResponse(
                    call: Call<ServerListData>,
                    response: Response<ServerListData>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        _serverData.postValue(result)
                        _isLoading.value = false
                    } else {
                        _isError.value = "Terjadi Kesalahan ${response.code()}"
                        _isLoading.value = false
                    }
                }

                override fun onFailure(call: Call<ServerListData>, t: Throwable) {
                    _isError.value = "Tidak Terkoneksi Internet"
                    _isLoading.value = false
                }
            })
    }

    fun requestServerDataSearch(
        token: String,
        search: String
    ) {
        _isError.value = ""
        _isLoading.value = true
        apiService.getServerListSearch(
            token = token,
            search = search
        )
            .enqueue(object : Callback<ServerListData> {
                override fun onResponse(
                    call: Call<ServerListData>,
                    response: Response<ServerListData>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        _serverData.postValue(result)
                        _isLoading.value = false
                    } else {
                        _isError.value = "Terjadi Kesalahan"
                        _isLoading.value = false
                    }
                }

                override fun onFailure(call: Call<ServerListData>, t: Throwable) {
                    _isError.value = "Tidak Terkoneksi Internet"
                    _isLoading.value = false
                }
            })
    }

    fun getServerData(): MutableLiveData<ServerListData> {
        return _serverData
    }
}