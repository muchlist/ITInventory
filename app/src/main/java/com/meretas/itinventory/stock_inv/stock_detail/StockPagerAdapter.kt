package com.meretas.itinventory.stock_inv.stock_detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.meretas.itinventory.stock_inv.stock_detail.stock_use_list_fragment.AdditionStockFragment
import com.meretas.itinventory.stock_inv.stock_detail.stock_use_list_fragment.ConsumeStockFragment
import com.meretas.itinventory.stock_inv.stock_detail.stock_use_list_fragment.DetailStockFragment

class StockPagerAdapter (fm: FragmentManager) : FragmentPagerAdapter(fm){

    private val pages = listOf(
        DetailStockFragment(),
        ConsumeStockFragment(),
        AdditionStockFragment()
    )

    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getCount(): Int {
        return pages.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "Detail"
            1 -> "Dipakai"
            else -> "Ditambahkan"
        }
    }
}