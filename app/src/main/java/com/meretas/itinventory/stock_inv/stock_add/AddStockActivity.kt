package com.meretas.itinventory.stock_inv.stock_add

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.meretas.itinventory.R
import com.meretas.itinventory.data.StockPostData
import com.meretas.itinventory.utils.App
import com.meretas.itinventory.utils.Statis
import kotlinx.android.synthetic.main.activity_add_stock.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

class AddStockActivity : AppCompatActivity() {

    private lateinit var viewModel: AddStockViewModel
    private lateinit var stock: StockPostData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_stock)

        viewModel = ViewModelProviders.of(this).get(AddStockViewModel::class.java)

        add_stock_title_branch.text = App.prefs.userBranchSave

        viewModel.isLoading.observe(this, Observer {
            if (it) {
                pb_frame_add_stock.visibility = View.VISIBLE
            } else {
                pb_frame_add_stock.visibility = View.GONE
            }
        })

        viewModel.isError.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                longToast(it)
            }
        })

        viewModel.isSuccess.observe(this, Observer {
            if (it) {
                val name = et_add_stock_name.text.toString()
                //untuk reload list atau tidak ketika sudah ditambahkan
                Statis.isStockUpdate = true
                longToast("$name Berhasil ditambahkan")
            }
        })

        viewModel.isCont.observe(this, Observer {
            if (!it) {
                finish()
            } else {
                et_add_stock_name.setText("")
            }
        })


        et_add_stock_category.setOnClickListener { choiceCategory() }


        //BUTTON BELUM DI ATUR MASIH SALAH
        bt_add_stock_continue.setOnClickListener { sendDataStock(true) }
        bt_add_stock_finish.setOnClickListener { sendDataStock(false) }
    }

    private fun sendDataStock(isContinue: Boolean) {
        if (et_add_stock_name.text.toString().isNotEmpty()) {
            stock = StockPostData(
                et_add_stock_name.text.toString(),
                et_add_stock_category.text.toString(),
                et_add_stock_thresold.text.toString().toInt(),
                et_add_stock_unit.text.toString(),
                true,
                et_add_stock_note.text.toString()
            )

            viewModel.postStock(stock, isContinue)

        } else {
            toast("Nama Stok Tidak Boleh Kosong!")
        }
    }


    private fun choiceCategory() {
        lateinit var dialog: AlertDialog
        val array = arrayOf(
            "Komputer",
            "Part Komputer",
            "UPS",
            "Printer",
            "CCTV",
            "Jaringan",
            "Server",
            "Handheld",
            "Display",
            "Presensi",
            "Lainnya"
        )
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Kategori Barang")
        builder.setSingleChoiceItems(array, -1) { _, which ->
            val status = array[which]
            try {
                et_add_stock_category.text = status
            } catch (e: IllegalArgumentException) {
                toast("error")
            }
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }
}
