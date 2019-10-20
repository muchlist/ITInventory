package com.meretas.itinventory.inv_computer.computer_add_history

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.meretas.itinventory.utils.INTENT_DETAIL_ADD_HISTORY_ID
import com.meretas.itinventory.utils.INTENT_DETAIL_ADD_HISTORY_NAME
import com.meretas.itinventory.utils.Statis
import kotlinx.android.synthetic.main.activity_history_add.*
import org.jetbrains.anko.toast


class AddHistoryComputerActivity : AppCompatActivity(), AddHistoryComputerView {

    private lateinit var historyComputerPresenter: AddHistoryComputerPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.meretas.itinventory.R.layout.activity_history_add)

        val intentID = intent.getIntExtra(INTENT_DETAIL_ADD_HISTORY_ID, 0)
        val intentName = intent.getStringExtra(INTENT_DETAIL_ADD_HISTORY_NAME)

        tv_add_history_name.text = "PC $intentName"

        historyComputerPresenter = AddHistoryComputerPresenter(this)

        //BUTTON ADD HISTORY
        bt_add_history.setOnClickListener {
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                currentFocus?.windowToken, HIDE_NOT_ALWAYS
            )

            val note: String = et_add_history_note.text.toString()
            val status = et_add_history_status.text.toString()
            historyComputerPresenter.postHistory(intentID, note, status)
        }

        //STATUS CHOICES
        et_add_history_status.setOnClickListener {
            showStatusDialog()
        }
    }

    override fun showResult() {
        Statis.isHistoryUpdate = true
        finish()
    }

    override fun showError(notif: String) {
        toast(notif)
    }

    override fun showLoading() {
        pb_frame_add_history.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        pb_frame_add_history.visibility = View.GONE
    }

    override fun onDestroy() {
        historyComputerPresenter.onDestroy()
        super.onDestroy()
    }

    private fun showStatusDialog() {
        lateinit var dialog: AlertDialog
        val array = arrayOf(
            "Diperbaiki",
            "Bermasalah",
            "Rusak",
            "Pindah Ruangan",
            "Ganti User",
            "Upgrade",
            "Downgrade",
            "Dibersihkan",
            "Digudangkan",
            "Dimusnahkan",
            "Hilang"
        )
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Status")
        builder.setSingleChoiceItems(array, -1) { _, which ->
            val status = array[which]
            try {
                et_add_history_status.text = status
            } catch (e: IllegalArgumentException) {
                toast("error")
            }
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }
}
