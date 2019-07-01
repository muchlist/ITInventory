package com.meretas.itinventory.login

interface LoginView {
    fun updateConnection(connected : Boolean)
    fun showLoading()
    fun showToast(notif : String)
    fun gotoDashboardActivity()
    fun hideLoading()
}