package com.meretas.itinventory.inv_cctv.cctv_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.meretas.itinventory.data.CctvListData
import com.meretas.itinventory.services.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CctvListViewModel(
    private val apiService: ApiService,
    application: Application
) : AndroidViewModel(application) {

    //STOCK Data untuk RecyclerView
    private val _cctvData: MutableLiveData<CctvListData> = MutableLiveData()
    /*val cctvData: LiveData<CctvListData>
        get() = _cctvData*/

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

    fun requestCctvData(
        token: String,
        branch: String,
        status: String
    ) {
        _isError.value = ""
        _isLoading.value = true
        apiService.getCctvList(
            token = token, //App.prefs.authTokenSave,
            branch = branch,
            status = status
        )
            .enqueue(object : Callback<CctvListData> {
                override fun onResponse(
                    call: Call<CctvListData>,
                    response: Response<CctvListData>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        _cctvData.postValue(result)
                        _isLoading.value = false
                    } else {
                        _isError.value = "Terjadi Kesalahan"
                        _isLoading.value = false
                    }
                }

                override fun onFailure(call: Call<CctvListData>, t: Throwable) {
                    _isError.value = "Tidak Terkoneksi Internet"
                    _isLoading.value = false
                }
            })
    }

    fun requestCctvDataSearch(
        token: String,
        search: String
    ) {
        _isError.value = ""
        _isLoading.value = true
        apiService.getCctvListSearch(
            token = token,
            search = search
        )
            .enqueue(object : Callback<CctvListData> {
                override fun onResponse(
                    call: Call<CctvListData>,
                    response: Response<CctvListData>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        _cctvData.postValue(result)
                        _isLoading.value = false
                    } else {
                        _isError.value = "Terjadi Kesalahan"
                        _isLoading.value = false
                    }
                }

                override fun onFailure(call: Call<CctvListData>, t: Throwable) {
                    _isError.value = "Tidak Terkoneksi Internet"
                    _isLoading.value = false
                }
            })
    }

    fun getCctvData(): MutableLiveData<CctvListData> {
        return _cctvData
    }
}