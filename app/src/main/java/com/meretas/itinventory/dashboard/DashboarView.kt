package com.meretas.itinventory.dashboard

import com.meretas.itinventory.data.HistoryListGeneralData

interface DashboarView {
    fun getUserInfo(name: String, branch: String, isReadOnly: Boolean)
    fun showToastAndReload(notif: String)
    fun showHistory(generalData: List<HistoryListGeneralData.Result>)
    fun hideProgressBarHistory()
    fun showProgressBarHistory()
}