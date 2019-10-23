package com.meretas.itinventory.inv_printer.printer_detail

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.meretas.itinventory.R
import com.meretas.itinventory.dashboard.HistoryDetailActivity
import com.meretas.itinventory.dashboard.HistoryGeneralAdapter
import com.meretas.itinventory.data.HistoryListGeneralData
import com.meretas.itinventory.data.PrinterListData
import com.meretas.itinventory.inv_printer.printer_edit.EditPrinterActivity
import com.meretas.itinventory.inv_printer.printer_history.AddPrinterHistoryActivity
import com.meretas.itinventory.services.Api
import com.meretas.itinventory.utils.*
import com.meretas.itinventory.utils.Statis.Companion.isHistoryUpdate
import com.meretas.itinventory.utils.Statis.Companion.isPrinterUpdate
import kotlinx.android.synthetic.main.activity_detail_printer.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class DetailPrinterActivity : AppCompatActivity() {

    //View Model
    private lateinit var viewModel: DetailPrinterViewModel
    private lateinit var viewModelFactory: DetailPrinterViewModelFactory

    //recyclerview
    private lateinit var historyPrinterAdapter: HistoryGeneralAdapter
    private var historyPrinterData: MutableList<HistoryListGeneralData.Result> = mutableListOf()

    //INTENT
    private lateinit var intentData: PrinterListData.Result

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_printer)
        //Intent dari stock list di transfer ke view model
        intentData = intent.getParcelableExtra(
            DATA_INTENT_PRINTER_LIST_TO_DETAIL
        )

        initViewModel()

        viewModelObserve()

        //INJECT DATA DARI INTENT KE VIEWMODEL
        viewModel.injectPrinterDataToViewModel(intentData)

        setRecyclerView()
        viewModel.getPrinterHistory(App.prefs.authTokenSave, intentData.id)

        //PERCABANGN TOMBOL MENURUT BRANCH
        if (App.prefs.userBranchSave != intentData.branch || App.prefs.isReadOnly) {
            bt_detail_printer_add_history.disable()
            bt_detail_edit_printer.disable()
        } else {
            bt_detail_printer_add_history.enable()
            bt_detail_edit_printer.enable()

            bt_detail_printer_add_history.setOnClickListener {
                startActivity<AddPrinterHistoryActivity>(
                    INTENT_DETAIL_ADD_HISTORY_PRINTER_ID to intentData.id,
                    INTENT_DETAIL_ADD_HISTORY_PRINTER_NAME to intentData.printerName
                )
            }

            bt_detail_edit_printer.setOnClickListener {
                startActivity<EditPrinterActivity>(
                    DATA_INTENT_PRINTER_DETAIL to viewModel.printerDetailData.value
                )
            }
        }

        iv_detail_back_printer.setOnClickListener {
            finish()
        }

    }

    private fun initViewModel() {
        val application = requireNotNull(this).application
        val apiService = Api.retrofitService
        viewModelFactory = DetailPrinterViewModelFactory(apiService, application)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(DetailPrinterViewModel::class.java)
    }

    private fun setRecyclerView() {
        rv_detail_printer_history.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        historyPrinterAdapter = HistoryGeneralAdapter(this, historyPrinterData) {
            startActivity<HistoryDetailActivity>(DATA_INTENT_DASHBOARD_DETAIL_HISTORY to it)
        }
        rv_detail_printer_history.adapter = historyPrinterAdapter
        rv_detail_printer_history.setHasFixedSize(true)
    }

    private fun viewModelObserve() {
        //LOADING PROGRESS
        viewModel.isLoading.observe(this, Observer {
            if (it) {
                pb_detail_printer_history.visibility = View.VISIBLE
            } else {
                pb_detail_printer_history.visibility = View.GONE
            }
        })

        //TOAST
        viewModel.isError.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                toast(it)
            }
        })

        //DETAIL
        viewModel.printerDetailData.observe(this, Observer {
            tv_detail_printer_name.text = it.printerName
            tv_detail_printer_ip_address.text = it.ipAddress
            tv_detail_printer_branch.text = it.branch
            tv_detail_printer_division.text = it.division
            tv_detail_printer_merk.text = it.merkModel
            tv_detail_printer_year.text = it.year
            tv_detail_printer_status.text = it.status
            tv_detail_printer_note.text = it.note
        })

        viewModel.printerDetailHistoryPrinterData.observe(this, Observer {
            it?.let {
                historyPrinterData.clear()
                historyPrinterData.addAll(it)
                historyPrinterAdapter.notifyDataSetChanged()

                //Declare And SetAnimation
                val topToBottom = AnimationUtils.loadAnimation(this, R.anim.fade_in_history)
                rv_detail_printer_history.startAnimation(topToBottom)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (isPrinterUpdate) {
            viewModel.getPrinterRefresh(App.prefs.authTokenSave, intentData.id)
        }

        if (isHistoryUpdate) {
            viewModel.getPrinterHistory(
                token = App.prefs.authTokenSave,
                printerId = intentData.id
            )
        }
    }
}
