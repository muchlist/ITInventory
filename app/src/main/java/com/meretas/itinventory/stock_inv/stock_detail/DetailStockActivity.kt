package com.meretas.itinventory.stock_inv.stock_detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.meretas.itinventory.R
import com.meretas.itinventory.data.StockListData
import com.meretas.itinventory.utils.DATA_INTENT_STOCK_LIST_DETAIL
import kotlinx.android.synthetic.main.activity_detail_stock.*

class DetailStockActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_stock)

        initPager()

        val intent = intent.getParcelableExtra<StockListData.Result>(
            DATA_INTENT_STOCK_LIST_DETAIL
        )

        with(intent) {
            tv_detail_stock_name.text = stockName
            tv_detail_stock_branch.text = branch
            tv_detail_stock_category.text = category
            tv_detail_stock_status.text = if (active) "Aktif" else "Nonaktif"
            tv_detail_stock_thresold.text = threshold.toString()
            tv_detail_stock_create_date.text = createdAt
            tv_detail_stock_unit.text = unit
            tv_detail_stock_note.text = note

            val added: Int = stockAdded ?: 0
            val used: Int = stockUsed ?: 0

            tv_detail_stock_additions.text = added.toString()
            tv_detail_stock_consume.text = used.toString()
            tv_detail_stock_resultstock.text = (added - used).toString()
        }

    }

    private fun initPager() {
        vp_detail_stock.adapter = StockPagerAdapter(supportFragmentManager)
        vp_detail_stock.offscreenPageLimit = 2
        tl_detail_stock.setupWithViewPager(vp_detail_stock)
    }
}
