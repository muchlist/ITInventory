package com.meretas.itinventory.inv_computer.computer_detail

import com.meretas.itinventory.data.HistoryListGeneralData

interface DetailComputerView {
    fun showLoadingHistory()
    fun hideLoadingHistory()
    fun showHistoryList(historyListGeneral: List<HistoryListGeneralData.Result>)
    fun showToastError(notif: String)
}