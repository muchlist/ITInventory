package com.meretas.itinventory.stock_inv.stock_list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.meretas.itinventory.R
import com.meretas.itinventory.data.StockListData
import com.meretas.itinventory.stock_inv.stock_add.AddStockActivity
import com.meretas.itinventory.stock_inv.stock_detail.DetailStockActivity
import com.meretas.itinventory.utils.App
import com.meretas.itinventory.utils.DATA_INTENT_STOCK_LIST_DETAIL
import com.meretas.itinventory.utils.Statis
import com.meretas.itinventory.utils.Statis.Companion.isComputerUpdate
import com.meretas.itinventory.utils.Statis.Companion.isStockChangeMinus
import com.meretas.itinventory.utils.Statis.Companion.isStockChangePlus
import com.meretas.itinventory.utils.Statis.Companion.isStockUpdate
import com.meretas.itinventory.utils.Statis.Companion.whatStockActiveStatus
import com.meretas.itinventory.utils.Statis.Companion.whatStockCategory
import kotlinx.android.synthetic.main.activity_computer_list.*
import kotlinx.android.synthetic.main.activity_stocklist.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class StocklistActivity : AppCompatActivity() {

    private lateinit var viewModel: StocklistViewModel
    private lateinit var stockAdapter: StocklistAdapter
    private var stockData: MutableList<StockListData.Result> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stocklist)

        viewModel = ViewModelProviders.of(this).get(StocklistViewModel::class.java)


        // DATA RECYCLERVIEW
        viewModel.stockData.observe(this, Observer {
            stockData.clear()
            stockData.addAll(it.results)
            stockAdapter.notifyDataSetChanged()
        })

        setRecyclerView()

        //MERUBAH DATA RECYCLER VIEW #1
        viewModel.getStockData(App.prefs.userBranchSave,whatStockCategory,whatStockActiveStatus)

        //BUTTON ADD
        bt_stocklist_tambah.setOnClickListener {
            startActivity<AddStockActivity>()
        }


        //LOADING
        viewModel.isLoading.observe(this, Observer {
            if (it) {
                pb_stocklist.visibility = View.VISIBLE
            } else {
                pb_stocklist.visibility = View.GONE
            }
        })

        //ERROR
        viewModel.isError.observe(this, Observer {
            if (!it.isNullOrEmpty()){
                toast(it)
            }
        })

    }

    private fun setRecyclerView(){
        rv_stocklist.layoutManager = LinearLayoutManager(this)
        stockAdapter = StocklistAdapter(this, stockData) {
            startActivity<DetailStockActivity>(
                DATA_INTENT_STOCK_LIST_DETAIL to it
            )
        }
        rv_stocklist.adapter = stockAdapter
        rv_stocklist.setHasFixedSize(true)
    }

    override fun onResume() {
        super.onResume()
        //JIKA STOCK UPDATE TRUE < HANYA BISA TRUE JIKA ADA PENAMBAHAN STOCK
        if (isStockUpdate || isStockChangeMinus || isStockChangePlus) {
            viewModel.getStockData(App.prefs.userBranchSave,whatStockCategory,whatStockActiveStatus)
            isStockUpdate = false
            isStockChangeMinus = false
            isStockChangePlus = false
        }
    }
}
