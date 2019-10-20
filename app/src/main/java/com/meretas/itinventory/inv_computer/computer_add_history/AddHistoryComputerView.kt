package com.meretas.itinventory.inv_computer.computer_add_history

interface AddHistoryComputerView {
    fun showResult()
    fun showError(notif: String)
    fun showLoading()
    fun hideLoading()
}