package com.meretas.itinventory.stock_inv.stock_detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class StockPagerAdapter (fm: FragmentManager) : FragmentPagerAdapter(fm){

    private val pages = listOf(
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
            0 -> "Dipakai"
            else -> "Ditambahkan"
        }
    }
}