package com.meretas.itinventory

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.meretas.itinventory.dashboard.DashboardActivity
import com.meretas.itinventory.login.LoginActivity
import com.meretas.itinventory.utils.App

class MainEmptyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityIntent: Intent

        if (App.prefs.authTokenSave.isNullOrEmpty()) {
            activityIntent = Intent(this, LoginActivity::class.java)
        } else {
            activityIntent = Intent(this, DashboardActivity::class.java)
        }

        startActivity(activityIntent)
        finish()

    }
}
