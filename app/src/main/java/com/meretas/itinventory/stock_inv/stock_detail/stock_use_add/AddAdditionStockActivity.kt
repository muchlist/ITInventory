package com.meretas.itinventory.stock_inv.stock_detail.stock_use_add

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.meretas.itinventory.R
import com.meretas.itinventory.data.AddAndConsumePostData
import com.meretas.itinventory.utils.INTENT_DETAIL_ADD_ADDITION_ID
import com.meretas.itinventory.utils.INTENT_DETAIL_ADD_ADDITION_NAME
import com.meretas.itinventory.utils.Statis
import kotlinx.android.synthetic.main.activity_add_addition_stock.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

class AddAdditionStockActivity : AppCompatActivity() {

    private lateinit var viewModel: AddAdditionStockViewModel
    private lateinit var additionData: AddAndConsumePostData
    private var intentID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_addition_stock)

        intentID = intent.getIntExtra(INTENT_DETAIL_ADD_ADDITION_ID, 0)
        val intentName = intent.getStringExtra(INTENT_DETAIL_ADD_ADDITION_NAME)

        viewModel = ViewModelProviders.of(this).get(AddAdditionStockViewModel::class.java)

        //Nama di judul activity
        addition_stock_name.text = intentName

        viewModel.isLoading.observe(this, Observer {
            if (it) {
                pb_frame_addition_stock.visibility = View.VISIBLE
            } else {
                pb_frame_addition_stock.visibility = View.GONE
            }
        })

        viewModel.isError.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                longToast(it)
            }
        })

        viewModel.isSuccess.observe(this, Observer {
            if (it) {
                val qty = et_addition_stock_qty.text.toString()
                longToast("Berhasil menambahkan $qty")

                //refresh detail stok
                Statis.isStockChangePlus = true
                finish()
            }
        })

        bt_addition_stock_save.setOnClickListener { sendDataStock() }
    }

    private fun sendDataStock() {
        if (et_addition_stock_event.text.toString().isNotEmpty() &&
            et_addition_stock_qty.text.toString().isNotEmpty() &&
            et_addition_stock_note.text.toString().isNotEmpty()
        ) {
            additionData = AddAndConsumePostData(
                et_addition_stock_event.text.toString(),
                et_addition_stock_note.text.toString(),
                et_addition_stock_qty.text.toString().toInt()
            )
            viewModel.postStock(additionData, intentID)

        } else {
            toast("Semua field harus terisi !")
        }
    }
}
