package com.meretas.itinventory.computer_inv.add_history

interface HistoryAddView {
    fun showResult()
    fun showError(notif : String)
    fun showLoading()
    fun hideLoading()
}