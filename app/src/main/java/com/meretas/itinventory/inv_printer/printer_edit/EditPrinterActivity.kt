package com.meretas.itinventory.inv_printer.printer_edit

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.meretas.itinventory.R
import com.meretas.itinventory.data.PrinterListData
import com.meretas.itinventory.services.Api
import com.meretas.itinventory.utils.App
import com.meretas.itinventory.utils.DATA_INTENT_PRINTER_DETAIL
import com.meretas.itinventory.utils.Statis
import kotlinx.android.synthetic.main.activity_edit_printer.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

class EditPrinterActivity : AppCompatActivity() {

    //View Model
    private lateinit var viewModel: EditPrinterViewModel
    private lateinit var viewModelFactory: EditPrinterViewModelFactory

    //Intent
    private lateinit var intentData: PrinterListData.Result

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_printer)

        //INTENT
        intentData = intent.getParcelableExtra(DATA_INTENT_PRINTER_DETAIL)

        initViewModel()
        viewModelObserve()

        //INIT VIEW
        intentData.let {
            edit_printer_title_name.text = it.printerName
            et_edit_printer_name.setText(it.printerName)
            et_edit_printer_ip.setText(it.ipAddress)
            et_edit_printer_division.text = it.division
            et_edit_printer_year.setText(it.year)
            et_edit_printer_merk.setText(it.merkModel)
            et_edit_printer_status.text = it.status
            et_edit_printer_note.setText(it.note)
        }

        bt_edit_printer_save.setOnClickListener {
            sendDataPrinter()
        }

        et_edit_printer_status.setOnClickListener { statusCategory() }
        et_edit_printer_division.setOnClickListener { divisionCategory() }


    }

    private fun divisionCategory() {
        lateinit var dialog: AlertDialog
        val arrayDropdown = resources.getStringArray(R.array.computer_division_array_post)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Divisi")
        builder.setSingleChoiceItems(arrayDropdown, -1) { _, which ->
            val division = arrayDropdown[which]
            try {
                et_edit_printer_division.text = division
            } catch (e: IllegalArgumentException) {
                toast("error")
            }
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }

    private fun statusCategory() {
        lateinit var dialog: AlertDialog
        val arrayDropdown = resources.getStringArray(R.array.printer_status_array_post)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Status")
        builder.setSingleChoiceItems(arrayDropdown, -1) { _, which ->
            val status = arrayDropdown[which]
            try {
                et_edit_printer_status.text = status
            } catch (e: IllegalArgumentException) {
                toast("error")
            }
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }

    private fun sendDataPrinter() {
        if (et_edit_printer_name.text.toString().isNotEmpty()) {
            val printer = PrinterListData.Result(
                id = intentData.id,
                printerName = et_edit_printer_name.text.toString(),
                ipAddress = et_edit_printer_ip.text.toString(),
                branch = intentData.branch,
                division = et_edit_printer_division.text.toString(),
                year = et_edit_printer_year.text.toString(),
                merkModel = et_edit_printer_merk.text.toString(),
                status = et_edit_printer_status.text.toString(),
                note = et_edit_printer_note.text.toString(),
                createdAt = "",
                updatedAt = ""
            )

            viewModel.putPrinter(App.prefs.authTokenSave, printer)

        } else {
            toast("Nama printer tidak boleh kosong!")
        }
    }

    private fun initViewModel() {
        val application = requireNotNull(this).application
        val apiService = Api.retrofitService
        viewModelFactory = EditPrinterViewModelFactory(apiService, application)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(EditPrinterViewModel::class.java)
    }

    private fun viewModelObserve() {
        viewModel.isLoading.observe(this, Observer {
            if (it) {
                pb_frame_edit_printer.visibility = View.VISIBLE
            } else {
                pb_frame_edit_printer.visibility = View.GONE
            }
        })

        viewModel.isError.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                longToast(it)
            }
        })

        viewModel.isSuccess.observe(this, Observer {
            if (it) {
                Statis.isPrinterUpdate = true
                finish()
            }
        })
    }
}
