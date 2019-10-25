package com.meretas.itinventory.inv_server.server_edit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.meretas.itinventory.R
import com.meretas.itinventory.data.ServerListData
import com.meretas.itinventory.services.Api
import com.meretas.itinventory.utils.App
import com.meretas.itinventory.utils.DATA_INTENT_SERVER_DETAIL
import com.meretas.itinventory.utils.Statis
import kotlinx.android.synthetic.main.activity_edit_server.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

class EditServerActivity : AppCompatActivity() {

    //View Model
    private lateinit var viewModel: EditServerViewModel
    private lateinit var viewModelFactory: EditServerViewModelFactory

    //Intent
    private lateinit var intentData: ServerListData.Result

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_server)

        //INTENT
        intentData = intent.getParcelableExtra(DATA_INTENT_SERVER_DETAIL)

        initViewModel()
        viewModelObserve()

        //INIT VIEW
        intentData.let {
            edit_server_title_name.text = it.serverName
            et_edit_server_name.setText(it.serverName)
            et_edit_server_ip.setText(it.ipAddress)
            et_edit_server_category.text = it.category
            et_edit_server_location.setText(it.location)
            et_edit_server_year.setText(it.year)
            et_edit_server_merk.setText(it.merkModel)
            et_edit_server_status.text = it.status
            et_edit_server_note.setText(it.note)
        }

        bt_edit_server_save.setOnClickListener {
            sendDataServer()
        }

        et_edit_server_status.setOnClickListener { dialogStatus() }
        et_edit_server_category.setOnClickListener { dialogCategory() }


    }

    private fun dialogCategory() {
        lateinit var dialog: AlertDialog
        val arrayDropdown = resources.getStringArray(R.array.server_category_post)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Divisi")
        builder.setSingleChoiceItems(arrayDropdown, -1) { _, which ->
            val category = arrayDropdown[which]
            try {
                et_edit_server_category.text = category
            } catch (e: IllegalArgumentException) {
                toast("error")
            }
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }

    private fun dialogStatus() {
        lateinit var dialog: AlertDialog
        val arrayDropdown = resources.getStringArray(R.array.cctv_status_array_post)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Status")
        builder.setSingleChoiceItems(arrayDropdown, -1) { _, which ->
            val status = arrayDropdown[which]
            try {
                et_edit_server_status.text = status
            } catch (e: IllegalArgumentException) {
                toast("error")
            }
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }

    private fun sendDataServer() {
        if (et_edit_server_name.text.toString().isNotEmpty()) {
            val server = ServerListData.Result(
                id = intentData.id,
                serverName = et_edit_server_name.text.toString(),
                ipAddress = et_edit_server_ip.text.toString(),
                branch = intentData.branch,
                category = et_edit_server_category.text.toString(),
                location = et_edit_server_location.text.toString(),
                year = et_edit_server_year.text.toString(),
                merkModel = et_edit_server_merk.text.toString(),
                status = et_edit_server_status.text.toString(),
                note = et_edit_server_note.text.toString(),
                createdAt = "",
                updatedAt = ""
            )

            viewModel.putServer(App.prefs.authTokenSave, server)

        } else {
            toast("Nama server tidak boleh kosong!")
        }
    }

    private fun initViewModel() {
        val application = requireNotNull(this).application
        val apiService = Api.retrofitService
        viewModelFactory = EditServerViewModelFactory(apiService, application)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(EditServerViewModel::class.java)
    }

    private fun viewModelObserve() {
        viewModel.isLoading.observe(this, Observer {
            if (it) {
                pb_frame_edit_server.visibility = View.VISIBLE
            } else {
                pb_frame_edit_server.visibility = View.GONE
            }
        })

        viewModel.isError.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                longToast(it)
            }
        })

        viewModel.isSuccess.observe(this, Observer {
            if (it) {
                Statis.isServerUpdate = true
                finish()
            }
        })
    }
}

