package com.meretas.itinventory.stock_inv.stock_detail.stock_use_add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.meretas.itinventory.R
import com.meretas.itinventory.data.AddAndConsumePostData
import com.meretas.itinventory.utils.*
import kotlinx.android.synthetic.main.activity_add_consume_stock.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

class AddConsumeStockActivity : AppCompatActivity() {

    private lateinit var viewModel: AddConsumeStockViewModel
    private lateinit var consumeData: AddAndConsumePostData
    private var intentID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_consume_stock)

        intentID = intent.getIntExtra(INTENT_DETAIL_ADD_CONSUME_ID, 0)
        val intentName = intent.getStringExtra(INTENT_DETAIL_ADD_CONSUME_NAME)

        viewModel = ViewModelProviders.of(this).get(AddConsumeStockViewModel::class.java)

        //Nama di judul activity
        consume_stock_name.text = intentName

        viewModel.isLoading.observe(this, Observer {
            if (it) {
                pb_frame_consume_stock.visibility = View.VISIBLE
            } else {
                pb_frame_consume_stock.visibility = View.GONE
            }
        })

        viewModel.isError.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                longToast(it)
            }
        })

        viewModel.isSuccess.observe(this, Observer {
            if (it) {
                val qty = et_consume_stock_qty.text.toString()
                longToast("Berhasil menambahkan $qty")

                //refresh detail stok
                Statis.isStockChangeMinus = true
                finish()
            }
        })

        bt_consume_stock_save.setOnClickListener { sendDataStock() }
    }

    private fun sendDataStock() {
        if (et_consume_stock_event.text.toString().isNotEmpty() &&
            et_consume_stock_qty.text.toString().isNotEmpty() &&
            et_consume_stock_note.text.toString().isNotEmpty()
        ) {
            consumeData = AddAndConsumePostData(
                et_consume_stock_event.text.toString(),
                et_consume_stock_note.text.toString(),
                et_consume_stock_qty.text.toString().toInt()
            )
            viewModel.postStock(consumeData, intentID)

        } else {
            toast("Semua field harus terisi !")
        }
    }
}
