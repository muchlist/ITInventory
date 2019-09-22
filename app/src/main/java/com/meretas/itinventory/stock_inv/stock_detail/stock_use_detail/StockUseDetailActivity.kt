package com.meretas.itinventory.stock_inv.stock_detail.stock_use_detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.meretas.itinventory.R
import com.meretas.itinventory.data.AddAndConsumeData
import com.meretas.itinventory.stock_inv.stock_detail.stock_use_edit.EditStockUseActivity
import com.meretas.itinventory.utils.*
import com.meretas.itinventory.utils.Statis.Companion.isStockChangeMinus
import com.meretas.itinventory.utils.Statis.Companion.isStockChangePlus
import com.meretas.itinventory.utils.Statis.Companion.isStockUseDetailUpdate
import kotlinx.android.synthetic.main.activity_stock_use_detail.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class StockUseDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: StockUseDetailViewModel
    private lateinit var intentFrom: String
    private lateinit var intentData: AddAndConsumeData.Result

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock_use_detail)

        //INIT VIEW MODEL
        viewModel = ViewModelProviders.of(this).get(StockUseDetailViewModel::class.java)


        //INTENT START
        intentFrom = intent.getStringExtra(
            SOURCE_INTENT_STOCK_USE
        )
        intentData = intent.getParcelableExtra(
            DATA_INTENT_STOCK_USE
        )
            //mengecek apakah data stock active
        val intentDataActive = intent.getBooleanExtra(SOURCE_INTENT_STOCK_ACTIVE, true)

        //INTENT END


        //INJECT DATA DARI INTENT KE VIEWMODEL
        viewModel.postDetailStockUse(intentData)


        //VIEWMODEL TO DISPLAY
        viewModel.stockUseDetailData.observe(this, Observer {
            tv_stockusedetail_name.text = it.stock
            tv_stockusedetail_no_ba.text = it.eventNumber
            tv_stockusedetail_status.text = it.qty.toString() + " " + it.unit
            tv_stockusedetail_note.text = it.note
            tv_stockusedetail_author.text = it.author
            tv_stockusedetail_date.text = it.createdAt

            buttonCheckState(it.branch, intentDataActive)
        })

        when (intentFrom) {
            FROM_ADDITION_STOCK -> tv_stockuse_status.text = "Ditambahkan"
            FROM_CONSUME_STOCK -> tv_stockuse_status.text = "Dipakai"
        }

        //TOMBOL CLOSE
        iv_stock_use_detail_close.setOnClickListener {
            onBackPressed()
        }


        //OBSERVER
        viewModel.isLoading.observe(this, Observer {
            if (it) {
                pb_frame_stock_use_detail.visibility = View.VISIBLE
            } else {
                pb_frame_stock_use_detail.visibility = View.GONE
            }
        })

        viewModel.isError.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                longToast(it)
            }
        })

        viewModel.isDeleteSuccess.observe(this, Observer {
            if (it) {
                when (intentFrom) {
                    FROM_ADDITION_STOCK -> isStockChangePlus = true
                    FROM_CONSUME_STOCK -> isStockChangeMinus = true
                }
                longToast("Berhasil dihapus")
                finish()
            }
        })

        //END OBSERVER

    }

    private fun buttonCheckState(branch: String, status: Boolean) {
        if (App.prefs.userBranchSave != branch || App.prefs.isReadOnly || !status) {
            bt_stock_use_edit.disable()
            bt_stock_use_delete.disable()
        } else {
            bt_stock_use_edit.enable()
            bt_stock_use_delete.enable()

            //TOMBOL EDIT
            bt_stock_use_edit.setOnClickListener {
                startActivity<EditStockUseActivity>(
                    SOURCE_INTENT_STOCK_USE to intentFrom,
                    DATA_INTENT_STOCK_USE to viewModel.stockUseDetailData.value
                )
            }
            //TOMBOL DELETE
            bt_stock_use_delete.setOnClickListener {
                toast("Tahan tombol 2 detik untuk menghapus")
            }

            bt_stock_use_delete.setOnLongClickListener {
                when (intentFrom) {
                    FROM_ADDITION_STOCK -> viewModel.deleteAddition(intentData.id)
                    FROM_CONSUME_STOCK -> viewModel.deleteConsume(intentData.id)
                }
                return@setOnLongClickListener true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isStockUseDetailUpdate) {
            when (intentFrom) {
                FROM_ADDITION_STOCK -> viewModel.getStockUseDetailRefreshFromAddition(
                    viewModel.stockUseDetailData.value?.id ?: 0
                )
                FROM_CONSUME_STOCK -> viewModel.getStockUseDetailRefreshFromConsume(
                    viewModel.stockUseDetailData.value?.id ?: 0
                )
            }

        }
    }
}
