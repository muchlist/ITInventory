package com.meretas.itinventory.inv_computer.computer_detail

import com.meretas.itinventory.data.HistoryListData

interface DetailComputerView {
    fun showLoadingHistory()
    fun hideLoadingHistory()
    fun showHistoryList(historyList: List<HistoryListData.Result>)
    fun showToastError(notif: String)
}