package com.meretas.itinventory.add_history

interface HistoryAddView {
    fun showResult()
    fun showError(notif : String)
    fun showLoading()
    fun hideLoading()
}