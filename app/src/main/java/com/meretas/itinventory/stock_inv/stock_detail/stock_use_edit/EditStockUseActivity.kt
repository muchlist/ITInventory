package com.meretas.itinventory.stock_inv.stock_detail.stock_use_edit

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.meretas.itinventory.R
import com.meretas.itinventory.data.AddAndConsumeData
import com.meretas.itinventory.data.AddAndConsumePostData
import com.meretas.itinventory.utils.*
import com.meretas.itinventory.utils.Statis.Companion.isStockUseDetailUpdate
import kotlinx.android.synthetic.main.activity_edit_stock_use.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

class EditStockUseActivity : AppCompatActivity() {

    private lateinit var viewModel: EditStockUseViewModel
    private lateinit var stockUseData: AddAndConsumePostData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_stock_use)

        //INIT VIEW MODEL
        viewModel = ViewModelProviders.of(this).get(EditStockUseViewModel::class.java)

        //INTENT
        val intentFrom = intent.getStringExtra(SOURCE_INTENT_STOCK_USE)
        val intentData = intent.getParcelableExtra<AddAndConsumeData.Result>(DATA_INTENT_STOCK_USE)

        //INIT DATA DARI DETAIL UNTUK DI EDIT
        when (intentFrom) {
            FROM_ADDITION_STOCK -> tv_edit_stock_use_title.text = getString(R.string.ubah_penambahan)
            FROM_CONSUME_STOCK -> tv_edit_stock_use_title.text = getString(R.string.ubah_pengurangan)
        }
        et_edit_stock_use_event.setText(intentData.eventNumber)
        et_edit_stock_use_qty.setText(intentData.qty.toString())
        et_edit_stock_use_note.setText(intentData.note)


        //OBSERVER START
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
                longToast("Berhasil Diubah")
                //refresh detail stok
                isStockUseDetailUpdate = true
                finish()
            }
        })
        //OBSERVER END

        //SAVE BUTTON
        bt_edit_stock_use_save.setOnClickListener {
            when (intentFrom) {
                FROM_ADDITION_STOCK -> sendDataStockAddition(intentData.id)
                FROM_CONSUME_STOCK -> sendDataStockConsume(intentData.id)
            }
        }

    }

    private fun sendDataStockAddition(id: Int) {
        if (et_edit_stock_use_event.text.toString().isNotEmpty() &&
            et_edit_stock_use_qty.text.toString().isNotEmpty() &&
            et_edit_stock_use_note.text.toString().isNotEmpty()
        ) {
            stockUseData = AddAndConsumePostData(
                et_edit_stock_use_event.text.toString(),
                et_edit_stock_use_note.text.toString(),
                et_edit_stock_use_qty.text.toString().toInt()
            )


            viewModel.editStockAddition(stockUseData, id)

        } else {
            toast("Semua field harus terisi !")
        }
    }

    private fun sendDataStockConsume(id: Int) {
        if (et_edit_stock_use_event.text.toString().isNotEmpty() &&
            et_edit_stock_use_qty.text.toString().isNotEmpty() &&
            et_edit_stock_use_note.text.toString().isNotEmpty()
        ) {
            stockUseData = AddAndConsumePostData(
                et_edit_stock_use_event.text.toString(),
                et_edit_stock_use_note.text.toString(),
                et_edit_stock_use_qty.text.toString().toInt()
            )
            viewModel.editStockConsume(stockUseData, id)

        } else {
            toast("Semua field harus terisi !")
        }
    }

}
