package com.meretas.itinventory.inv_server.server_history

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.meretas.itinventory.R
import com.meretas.itinventory.services.Api
import com.meretas.itinventory.utils.App
import com.meretas.itinventory.utils.INTENT_DETAIL_ADD_HISTORY_SERVER_ID
import com.meretas.itinventory.utils.INTENT_DETAIL_ADD_HISTORY_SERVER_NAME
import com.meretas.itinventory.utils.Statis
import kotlinx.android.synthetic.main.activity_history_add.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

class AddServerHistoryActivity : AppCompatActivity() {

    //View Model
    private lateinit var viewModel: AddServerHistoryViewModel
    private lateinit var viewModelFactory: AddServerHistoryViewModelfactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_add)

        val intentID = intent.getIntExtra(INTENT_DETAIL_ADD_HISTORY_SERVER_ID, 0)
        val intentName = intent.getStringExtra(INTENT_DETAIL_ADD_HISTORY_SERVER_NAME)

        tv_add_history_name.text = "Server $intentName"

        initViewModel()

        viewModelObserver()

        bt_add_history.setOnClickListener {
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
            )
            val note: String = et_add_history_note.text.toString()
            val status = et_add_history_status.text.toString()
            if (note.isNotEmpty()) {
                viewModel.postHistory(App.prefs.authTokenSave, intentID, note, status)
            } else {
                toast("Catatan tidak boleh kosong")
            }
        }
        et_add_history_status.setOnClickListener {
            showStatusDialog()
        }
    }

    private fun initViewModel() {
        val application = requireNotNull(this).application
        val apiService = Api.retrofitService
        viewModelFactory = AddServerHistoryViewModelfactory(apiService, application)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(AddServerHistoryViewModel::class.java)
    }

    private fun viewModelObserver() {
        viewModel.isLoading.observe(this, Observer {
            if (it) {
                pb_frame_add_history.visibility = View.VISIBLE
            } else {
                pb_frame_add_history.visibility = View.GONE
            }
        })

        viewModel.isError.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                longToast(it)
            }
        })

        viewModel.isSuccess.observe(this, Observer {
            if (it) {
                //untuk reload list ketika history berhasil ditambahkan
                Statis.isHistoryUpdate = true
                finish()
            }
        })
    }

    private fun showStatusDialog() {
        lateinit var dialog: AlertDialog
        val array = resources.getStringArray(R.array.server_history_status)
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