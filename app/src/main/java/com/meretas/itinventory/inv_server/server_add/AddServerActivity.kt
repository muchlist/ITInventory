package com.meretas.itinventory.inv_server.server_add

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.meretas.itinventory.R
import com.meretas.itinventory.data.ServerPostData
import com.meretas.itinventory.services.Api
import com.meretas.itinventory.utils.App
import com.meretas.itinventory.utils.Statis
import kotlinx.android.synthetic.main.activity_add_server.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

class AddServerActivity : AppCompatActivity() {

    //View Model
    private lateinit var viewModel: AddServerViewModel
    private lateinit var viewModelFactory: AddServerViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_server)
        initViewModel()
        viewModelObserve()

        add_server_title_branch.text = App.prefs.userBranchSave

        et_add_server_status.setOnClickListener { statusDialog() }
        et_add_server_category.setOnClickListener { categoryDialog() }

        //BUTTON SAVE
        bt_add_server_continue.setOnClickListener { sendDataServer(true) }
        bt_add_server_finish.setOnClickListener { sendDataServer(false) }

    }

    private fun initViewModel() {
        val application = requireNotNull(this).application
        val apiService = Api.retrofitService
        viewModelFactory = AddServerViewModelFactory(apiService, application)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(AddServerViewModel::class.java)
    }

    private fun viewModelObserve() {
        viewModel.isLoading.observe(this, Observer {
            if (it) {
                pb_frame_add_server.visibility = View.VISIBLE
            } else {
                pb_frame_add_server.visibility = View.GONE
            }
        })

        viewModel.isError.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                longToast(it)
            }
        })

        viewModel.isSuccess.observe(this, Observer {
            if (it) {
                val name = et_add_server_name.text.toString()
                //untuk reload list atau tidak ketika sudah ditambahkan
                Statis.isServerUpdate = true
                longToast("$name Berhasil ditambahkan")
            }
        })

        viewModel.isCont.observe(this, Observer {
            if (!it) {
                finish()
            } else {
                et_add_server_name.setText("")
                et_add_server_note.setText("")
                et_add_server_location.setText("")
            }
        })
    }

    private fun statusDialog() {
        lateinit var dialog: AlertDialog
        val arrayDropdown = resources.getStringArray(R.array.cctv_status_array_post)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Status")
        builder.setSingleChoiceItems(arrayDropdown, -1) { _, which ->
            val status = arrayDropdown[which]
            try {
                et_add_server_status.text = status
            } catch (e: IllegalArgumentException) {
                toast("error")
            }
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }

    private fun categoryDialog() {
        lateinit var dialog: AlertDialog
        val arrayDropdown = resources.getStringArray(R.array.server_category_post)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Kategori")
        builder.setSingleChoiceItems(arrayDropdown, -1) { _, which ->

            val category = arrayDropdown[which]
            try {
                et_add_server_category.text = category
            } catch (e: IllegalArgumentException) {
                toast("error")
            }

            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }

    private fun sendDataServer(isContinue: Boolean) {
        if (et_add_server_name.text.toString().isNotEmpty()) {
            val server = ServerPostData(
                token = App.prefs.authTokenSave,
                serverName = et_add_server_name.text.toString(),
                ipAddress = et_add_server_ip.text.toString(),
                category = et_add_server_category.text.toString(),
                year = et_add_server_year.text.toString(),
                merkModel = et_add_server_merk.text.toString(),
                status = et_add_server_status.text.toString(),
                location = et_add_server_location.text.toString(),
                note = et_add_server_note.text.toString()
            )

            viewModel.postServer(server, isContinue)

        } else {
            toast("Nama server tidak boleh kosong!")
        }
    }
}
