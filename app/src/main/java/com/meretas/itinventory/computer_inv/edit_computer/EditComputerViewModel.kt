package com.meretas.itinventory.computer_inv.edit_computer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.meretas.itinventory.data.ComputerListData
import com.meretas.itinventory.data.ComputerPostData
import com.meretas.itinventory.services.Api
import com.meretas.itinventory.utils.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditComputerViewModel : ViewModel() {

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


    fun putComputer(computer: ComputerPostData, id: Int) {

        _isLoading.value = true
        _isError.value = ""

        Api.retrofitService.putComputer(
            App.prefs.authTokenSave,
            id,
            computer.namaUser,
            computer.hostKomputer,
            computer.alamatIp,
            computer.nomerInventaris,
            computer.lokasi,
            computer.divisi,
            computer.seatManajement,
            computer.tahun,
            computer.merkPerangkat,
            computer.jenisPerangkat,
            computer.processor,
            computer.ram,
            computer.hardisk,
            computer.vga,
            computer.sistemOperasi,
            computer.statusPC,
            computer.note
        ).enqueue(object : Callback<ComputerListData.Result> {

            override fun onResponse(
                call: Call<ComputerListData.Result>,
                response: Response<ComputerListData.Result>
            ) {
                if (response.isSuccessful) {
                    _isSuccess.value = true
                    _isLoading.value = false
                } else {
                    _isError.value = response.code().toString()
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<ComputerListData.Result>, t: Throwable) {
                _isSuccess.value = false
                _isLoading.value = false
                _isError.value = t.toString()
            }
        })
    }
}