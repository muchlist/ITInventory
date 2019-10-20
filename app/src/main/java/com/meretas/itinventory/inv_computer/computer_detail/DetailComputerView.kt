package com.meretas.itinventory.inv_computer.computer_detail

import com.meretas.itinventory.data.HistoryListComputerData

interface DetailComputerView {
    fun showLoadingHistory()
    fun hideLoadingHistory()
    fun showHistoryList(historyListComputer: List<HistoryListComputerData.Result>)
    fun showToastError(notif: String)
}