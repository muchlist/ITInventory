package com.meretas.itinventory.computer_inv.computer_list

import com.meretas.itinventory.data.ComputerListData

interface ComputerListView {
    fun hideLoading()
    fun showLoading()
    fun showToast(notif: String)
    fun showComputers(data: List<ComputerListData.Result>)
    fun updateUnit(unit: String)
}