package com.meretas.itinventory.stock_inv.stock_edit

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.meretas.itinventory.R
import com.meretas.itinventory.data.StockListData
import com.meretas.itinventory.data.StockPostData
import com.meretas.itinventory.utils.DATA_INTENT_STOCK_DETAIL
import com.meretas.itinventory.utils.Statis
import kotlinx.android.synthetic.main.activity_edit_stock.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

class EditStockActivity : AppCompatActivity() {

    private lateinit var viewModel: EditStockViewModel
    private lateinit var stock: StockPostData
    private lateinit var intentData: StockListData.Result

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_stock)

        //INIT VIEW MODEL
        viewModel = ViewModelProviders.of(this).get(EditStockViewModel::class.java)

        //INTENT
        intentData = intent.getParcelableExtra(DATA_INTENT_STOCK_DETAIL)

        //INIT VIEW
        tv_edit_stock_title_name.text = intentData.stockName
        et_edit_stock_name.setText(intentData.stockName)
        et_edit_stock_category.text = intentData.category
        et_edit_stock_thresold.setText(intentData.threshold.toString())
        et_edit_stock_unit.setText(intentData.unit)
        et_edit_stock_note.setText(intentData.note)


        viewModel.isLoading.observe(this, Observer {
            if (it) {
                pb_frame_edit_stock.visibility = View.VISIBLE
            } else {
                pb_frame_edit_stock.visibility = View.GONE
            }
        })

        viewModel.isError.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                longToast(it)
            }
        })

        viewModel.isSuccess.observe(this, Observer {
            if (it) {
                //untuk reload list atau tidak ketika sudah ditambahkan
                Statis.isStockUpdate = true
                finish()
            }
        })

        //STOK CATEGORY CHOICES
        et_edit_stock_category.setOnClickListener { choiceCategory() }

        //BUTTON SAVE
        bt_edit_stock_continue.setOnClickListener { sendDataStock() }
    }

    private fun sendDataStock() {
        if (et_edit_stock_name.text.toString().isNotEmpty()) {
            stock = StockPostData(
                et_edit_stock_name.text.toString(),
                et_edit_stock_category.text.toString(),
                et_edit_stock_thresold.text.toString().toInt(),
                et_edit_stock_unit.text.toString(),
                true,
                et_edit_stock_note.text.toString(),
                intentData.id
            )

            viewModel.postStock(stock)

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
                et_edit_stock_category.text = status
            } catch (e: IllegalArgumentException) {
                toast("error")
            }
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }
}
