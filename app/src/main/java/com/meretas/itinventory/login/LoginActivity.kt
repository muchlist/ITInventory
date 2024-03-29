package com.meretas.itinventory.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.meretas.itinventory.R
import com.meretas.itinventory.dashboard.DashboardActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity(), LoginView {

    //presenter
    private lateinit var presenter: LoginPresenter

    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        presenter = LoginPresenter(this)

        presenter.getStatusConnection()

        btn_login_login.setOnClickListener {
            val username = edt_username_login.editText?.text.toString().toLowerCase()
            val password = edt_password_login.editText?.text.toString()
            if (username.isEmpty() || password.isEmpty()) {
                toast("Username atau Password tidak boleh kosong")
            } else {
                presenter.getToken(username, password)
            }
        }

        iv_status_login.setOnClickListener {
            presenter.getStatusConnection()
        }

    }


    override fun updateConnection(connected: Boolean) {
        if (connected) {
            iv_status_login.setImageResource(R.drawable.ic_baseline_check_box_24px)
        } else {
            iv_status_login.setImageResource(R.drawable.ic_baseline_indeterminate_check_box_24px)
        }
    }

    override fun gotoDashboardActivity() {
        startActivity<DashboardActivity>()
        finish()
    }

    override fun showLoading() {
        btn_login_login.visibility = View.INVISIBLE
        pb_login.visibility = View.VISIBLE

    }

    override fun showToast(notif: String) {
        toast(notif)
        //view.snackbar("Hello!")
    }

    override fun hideLoading() {
        pb_login.visibility = View.GONE
        btn_login_login.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}
