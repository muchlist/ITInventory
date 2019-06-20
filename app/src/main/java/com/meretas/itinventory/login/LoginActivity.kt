package com.meretas.itinventory.login

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.meretas.itinventory.R
import com.meretas.itinventory.services.ApiService
import com.meretas.itinventory.services.ServiceBuilder

class LoginActivity : AppCompatActivity(), LoginView {

    //presenter
    private lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val apiService: ApiService = ServiceBuilder.buildService(ApiService::class.java)
        presenter = LoginPresenter(this, apiService)

        presenter.getStatusConnection()
    }


    override fun updateConnection(connected: Boolean) {
        if (connected) {
            Toast.makeText(this, "berhasil", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "gagal", Toast.LENGTH_SHORT).show()
        }
    }

    override fun showLoading() {
    }

    override fun showToast(notif: String) {
    }

    override fun hideLoading() {
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}
