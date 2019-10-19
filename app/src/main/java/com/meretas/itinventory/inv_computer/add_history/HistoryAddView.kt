package com.meretas.itinventory.inv_computer.add_history

interface HistoryAddView {
    fun showResult()
    fun showError(notif: String)
    fun showLoading()
    fun hideLoading()
}