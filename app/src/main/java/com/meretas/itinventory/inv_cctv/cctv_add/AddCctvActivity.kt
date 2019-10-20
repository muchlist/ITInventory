package com.meretas.itinventory.inv_cctv.cctv_add

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.meretas.itinventory.R
import com.meretas.itinventory.data.CctvPostData
import com.meretas.itinventory.services.Api
import com.meretas.itinventory.utils.App
import com.meretas.itinventory.utils.Statis
import kotlinx.android.synthetic.main.activity_add_cctv.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

class AddCctvActivity : AppCompatActivity() {

    //View Model
    private lateinit var viewModel: AddCctvViewModel
    private lateinit var viewModelFactory: AddCctvViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_cctv)

        initViewModel()
        viewModelObserve()

        add_cctv_title_branch.text = App.prefs.userBranchSave

        et_add_cctv_status.setOnClickListener { statusCategory() }

        //BUTTON SAVE
        bt_add_cctv_continue.setOnClickListener { sendDataStock(true) }
        bt_add_cctv_finish.setOnClickListener { sendDataStock(false) }

    }

    private fun initViewModel() {
        val application = requireNotNull(this).application
        val apiService = Api.retrofitService
        viewModelFactory = AddCctvViewModelFactory(apiService, application)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(AddCctvViewModel::class.java)
    }

    private fun viewModelObserve() {
        viewModel.isLoading.observe(this, Observer {
            if (it) {
                pb_frame_add_cctv.visibility = View.VISIBLE
            } else {
                pb_frame_add_cctv.visibility = View.GONE
            }
        })

        viewModel.isError.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                longToast(it)
            }
        })

        viewModel.isSuccess.observe(this, Observer {
            if (it) {
                val name = et_add_cctv_name.text.toString()
                //untuk reload list atau tidak ketika sudah ditambahkan
                Statis.isCctvUpdate = true
                longToast("$name Berhasil ditambahkan")
            }
        })

        viewModel.isCont.observe(this, Observer {
            if (!it) {
                finish()
            } else {
                et_add_cctv_name.setText("")
                et_add_cctv_ip.setText("")
            }
        })
    }

    private fun statusCategory() {
        lateinit var dialog: AlertDialog
        val arrayDropdown = resources.getStringArray(R.array.cctv_status_array_post)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Status")
        builder.setSingleChoiceItems(arrayDropdown, -1) { _, which ->
            val status = arrayDropdown[which]
            try {
                et_add_cctv_status.text = status
            } catch (e: IllegalArgumentException) {
                toast("error")
            }
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }

    private fun sendDataStock(isContinue: Boolean) {
        if (et_add_cctv_name.text.toString().isNotEmpty()) {
            val cctv = CctvPostData(
                token = App.prefs.authTokenSave,
                cctvName = et_add_cctv_name.text.toString(),
                ipAddress = et_add_cctv_ip.text.toString(),
                location = et_add_cctv_location.text.toString(),
                year = et_add_cctv_year.text.toString(),
                merkModel = et_add_cctv_merk.text.toString(),
                status = et_add_cctv_status.text.toString(),
                note = et_add_cctv_note.text.toString()
            )

            viewModel.postCctv(cctv, isContinue)

        } else {
            toast("Nama cctv tidak boleh kosong!")
        }
    }
}
