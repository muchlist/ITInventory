package com.meretas.itinventory.inv_printer.printer_add

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.meretas.itinventory.R
import com.meretas.itinventory.data.PrinterPostData
import com.meretas.itinventory.services.Api
import com.meretas.itinventory.utils.App
import com.meretas.itinventory.utils.Statis
import kotlinx.android.synthetic.main.activity_add_printer.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

class AddPrinterActivity : AppCompatActivity() {

    //View Model
    private lateinit var viewModel: AddPrinterViewModel
    private lateinit var viewModelFactory: AddPrinterViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_printer)
        initViewModel()
        viewModelObserve()

        add_printer_title_branch.text = App.prefs.userBranchSave

        et_add_printer_status.setOnClickListener { statusCategory() }
        et_add_printer_division.setOnClickListener { divisionCategory() }

        //BUTTON SAVE
        bt_add_printer_continue.setOnClickListener { sendDataPrinter(true) }
        bt_add_printer_finish.setOnClickListener { sendDataPrinter(false) }

    }

    private fun initViewModel() {
        val application = requireNotNull(this).application
        val apiService = Api.retrofitService
        viewModelFactory = AddPrinterViewModelFactory(apiService, application)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(AddPrinterViewModel::class.java)
    }

    private fun viewModelObserve() {
        viewModel.isLoading.observe(this, Observer {
            if (it) {
                pb_frame_add_printer.visibility = View.VISIBLE
            } else {
                pb_frame_add_printer.visibility = View.GONE
            }
        })

        viewModel.isError.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                longToast(it)
            }
        })

        viewModel.isSuccess.observe(this, Observer {
            if (it) {
                val name = et_add_printer_name.text.toString()
                //untuk reload list atau tidak ketika sudah ditambahkan
                Statis.isPrinterUpdate = true
                longToast("$name Berhasil ditambahkan")
            }
        })

        viewModel.isCont.observe(this, Observer {
            if (!it) {
                finish()
            } else {
                et_add_printer_name.setText("")
                et_add_printer_ip.setText("")
                et_add_printer_merk.setText("")
            }
        })
    }

    private fun statusCategory() {
        lateinit var dialog: AlertDialog
        val arrayDropdown = resources.getStringArray(R.array.printer_status_array_post)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Status")
        builder.setSingleChoiceItems(arrayDropdown, -1) { _, which ->
            val status = arrayDropdown[which]
            try {
                et_add_printer_status.text = status
            } catch (e: IllegalArgumentException) {
                toast("error")
            }
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }

    private fun divisionCategory() {
        lateinit var dialog: AlertDialog
        val arrayDropdown = resources.getStringArray(R.array.computer_division_array_post)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Divisi")
        builder.setSingleChoiceItems(arrayDropdown, -1) { _, which ->
            val division = arrayDropdown[which]
            try {
                et_add_printer_division.text = division
            } catch (e: IllegalArgumentException) {
                toast("error")
            }
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }

    private fun sendDataPrinter(isContinue: Boolean) {
        if (et_add_printer_name.text.toString().isNotEmpty()) {
            val printer = PrinterPostData(
                token = App.prefs.authTokenSave,
                printerName = et_add_printer_name.text.toString(),
                ipAddress = et_add_printer_ip.text.toString(),
                division = et_add_printer_division.text.toString(),
                year = et_add_printer_year.text.toString(),
                merkModel = et_add_printer_merk.text.toString(),
                status = et_add_printer_status.text.toString(),
                note = et_add_printer_note.text.toString()
            )

            viewModel.postPrinter(printer, isContinue)

        } else {
            toast("Nama printer tidak boleh kosong!")
        }
    }
}
