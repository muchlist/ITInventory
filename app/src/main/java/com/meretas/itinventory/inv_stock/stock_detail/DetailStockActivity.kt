package com.meretas.itinventory.inv_stock.stock_detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.meretas.itinventory.R
import com.meretas.itinventory.data.StockListData
import com.meretas.itinventory.services.Api
import com.meretas.itinventory.inv_stock.stock_detail.stock_use_add.AddAdditionStockActivity
import com.meretas.itinventory.inv_stock.stock_detail.stock_use_add.AddConsumeStockActivity
import com.meretas.itinventory.utils.*
import kotlinx.android.synthetic.main.activity_detail_stock.*
import org.jetbrains.anko.startActivity

class DetailStockActivity : AppCompatActivity() {

    //View Model
    private lateinit var viewModel: DetailStockViewModel
    private lateinit var viewModelFactory: DetailStockViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_stock)

        initPager()

        //INIT VIEW MODEL
        val application = requireNotNull(this).application
        val apiService = Api.retrofitService
        viewModelFactory = DetailStockViewModelFactory(apiService, application)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(DetailStockViewModel::class.java)


        //Intent dari stock list di transfer ke view model
        val intent = intent.getParcelableExtra<StockListData.Result>(
            DATA_INTENT_STOCK_LIST_DETAIL
        )

        //INJECT DATA DARI INTENT KE VIEWMODEL
        viewModel.postDetailStock(intent)


        viewModel.stockDetailData.observe(this, Observer {
            //PERCABANGN TOMBOL MENURUT BRANCH
            if (App.prefs.userBranchSave != it.branch || App.prefs.isReadOnly || !it.active) {
                bt_detail_stock_add.disable()
                bt_detail_stock_consume.disable()
            } else {
                bt_detail_stock_add.enable()
                bt_detail_stock_consume.enable()

                bt_detail_stock_add.setOnClickListener {
                    startActivity<AddAdditionStockActivity>(
                        INTENT_DETAIL_ADD_ADDITION_ID to intent.id,
                        INTENT_DETAIL_ADD_ADDITION_NAME to intent.stockName
                    )
                }

                bt_detail_stock_consume.setOnClickListener {
                    startActivity<AddConsumeStockActivity>(
                        INTENT_DETAIL_ADD_CONSUME_ID to intent.id,
                        INTENT_DETAIL_ADD_CONSUME_NAME to intent.stockName
                    )
                }
            }
        })


    }

    private fun initPager() {
        vp_detail_stock.adapter = StockPagerAdapter(supportFragmentManager)
        vp_detail_stock.offscreenPageLimit = 2
        tl_detail_stock.setupWithViewPager(vp_detail_stock)
    }

}
