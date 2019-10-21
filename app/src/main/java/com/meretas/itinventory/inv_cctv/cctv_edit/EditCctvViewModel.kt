package com.meretas.itinventory.inv_cctv.cctv_edit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.meretas.itinventory.data.CctvListData
import com.meretas.itinventory.services.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditCctvViewModel(
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

    fun putCctv(token : String, cctv: CctvListData.Result) {

        _isLoading.value = true
        _isError.value = ""

        apiService.putCctv(
            token = token,
            id = cctv.id,
            cctvName = cctv.cctvName,
            ipAddress = cctv.ipAddress,
            location = cctv.location,
            year = cctv.year,
            merkModel = cctv.merkModel,
            status = cctv.status,
            note = cctv.note
        ).enqueue(object : Callback<CctvListData.Result> {

            override fun onResponse(
                call: Call<CctvListData.Result>,
                response: Response<CctvListData.Result>
            ) {
                if (response.isSuccessful) {
                    _isSuccess.value = true
                    _isLoading.value = false
                } else {
                    _isError.value = "Cctv gagal dirubah"
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<CctvListData.Result>, t: Throwable) {
                _isSuccess.value = false
                _isLoading.value = false
                _isError.value = t.toString()
            }
        })
    }
}