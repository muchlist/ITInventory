package com.meretas.itinventory.dashboard

import com.meretas.itinventory.data.HistoryListComputerData

interface DashboarView {
    fun getUserInfo(name: String, branch: String, isReadOnly: Boolean)
    fun showToastAndReload(notif: String)
    fun showHistory(computerData: List<HistoryListComputerData.Result>)
    fun hideProgressBarHistory()
    fun showProgressBarHistory()
}