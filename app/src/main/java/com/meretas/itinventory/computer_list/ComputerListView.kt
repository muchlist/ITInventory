package com.meretas.itinventory.computer_list

import com.meretas.itinventory.data.ComputerListData

interface ComputerListView {
    fun hideLoading()
    fun showLoading()
    fun showToast(notif : String)
    fun showComputers(data: List<ComputerListData.Result>)
    fun showUpdateComputers()
    fun hideLocationChoices()
    fun showLocationChoices()
    fun updateUnit(unit: String)
}