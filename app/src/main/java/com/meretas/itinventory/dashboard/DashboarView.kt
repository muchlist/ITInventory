package com.meretas.itinventory.dashboard

import com.meretas.itinventory.data.HistoryListData

interface DashboarView {
    fun getUserInfo(name: String,branch: String)
    fun showToast(notif : String)
    fun showHistory(data: List<HistoryListData.Result>)
    fun hideProgressBarHistory()
    fun showProgressBarHistory()
}