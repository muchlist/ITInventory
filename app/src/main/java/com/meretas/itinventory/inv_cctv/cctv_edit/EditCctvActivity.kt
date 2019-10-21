package com.meretas.itinventory.inv_cctv.cctv_edit

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.meretas.itinventory.R
import com.meretas.itinventory.data.CctvListData
import com.meretas.itinventory.services.Api
import com.meretas.itinventory.utils.App
import com.meretas.itinventory.utils.DATA_INTENT_CCTV_DETAIL
import com.meretas.itinventory.utils.Statis
import kotlinx.android.synthetic.main.activity_edit_cctv.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

class EditCctvActivity : AppCompatActivity() {

    //View Model
    private lateinit var viewModel: EditCctvViewModel
    private lateinit var viewModelFactory: EditCctvViewModelFactory

    //Intent
    private lateinit var intentData: CctvListData.Result

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_cctv)

        //INTENT
        intentData = intent.getParcelableExtra(DATA_INTENT_CCTV_DETAIL)

        initViewModel()
        viewModelObserve()

        //INIT VIEW
        intentData.let {
            edit_cctv_title_name.text = it.cctvName
            et_edit_cctv_name.setText(it.cctvName)
            et_edit_cctv_ip.setText(it.ipAddress)
            et_edit_cctv_location.setText(it.location)
            et_edit_cctv_year.setText(it.year)
            et_edit_cctv_merk.setText(it.merkModel)
            et_edit_cctv_status.text = it.status
            et_edit_cctv_note.setText(it.note)
        }

        bt_edit_cctv_save.setOnClickListener {
            sendDataCctv()
        }

        et_edit_cctv_status.setOnClickListener { statusCategory() }


    }

    private fun statusCategory() {
        lateinit var dialog: AlertDialog
        val arrayDropdown = resources.getStringArray(R.array.cctv_status_array_post)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Status")
        builder.setSingleChoiceItems(arrayDropdown, -1) { _, which ->
            val status = arrayDropdown[which]
            try {
                et_edit_cctv_status.text = status
            } catch (e: IllegalArgumentException) {
                toast("error")
            }
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }

    private fun sendDataCctv() {
        if (et_edit_cctv_name.text.toString().isNotEmpty()) {
            val cctv = CctvListData.Result(
                id = intentData.id,
                cctvName = et_edit_cctv_name.text.toString(),
                ipAddress = et_edit_cctv_ip.text.toString(),
                branch = intentData.branch,
                location = et_edit_cctv_location.text.toString(),
                year = et_edit_cctv_year.text.toString(),
                merkModel = et_edit_cctv_merk.text.toString(),
                status = et_edit_cctv_status.text.toString(),
                note = et_edit_cctv_note.text.toString(),
                createdAt = "",
                updatedAt = ""
            )

            viewModel.putCctv(App.prefs.authTokenSave, cctv)

        } else {
            toast("Nama cctv tidak boleh kosong!")
        }
    }

    private fun initViewModel() {
        val application = requireNotNull(this).application
        val apiService = Api.retrofitService
        viewModelFactory = EditCctvViewModelFactory(apiService, application)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(EditCctvViewModel::class.java)
    }

    private fun viewModelObserve() {
        viewModel.isLoading.observe(this, Observer {
            if (it) {
                pb_frame_edit_cctv.visibility = View.VISIBLE
            } else {
                pb_frame_edit_cctv.visibility = View.GONE
            }
        })

        viewModel.isError.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                longToast(it)
            }
        })

        viewModel.isSuccess.observe(this, Observer {
            if (it) {
                Statis.isCctvUpdate = true
                finish()
            }
        })
    }
}
