package com.meretas.itinventory.computer_detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.meretas.itinventory.R
import com.meretas.itinventory.dashboard.HistoryAdapter
import com.meretas.itinventory.data.ComputerListData
import com.meretas.itinventory.data.HistoryListData
import com.meretas.itinventory.add_history.HistoryAddActivity
import com.meretas.itinventory.edit_computer.EditComputerActivity
import com.meretas.itinventory.history.HistoryDetailActivity
import com.meretas.itinventory.utils.*
import kotlinx.android.synthetic.main.activity_computer_list.*
import kotlinx.android.synthetic.main.activity_detail_computer.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class DetailComputerActivity : AppCompatActivity(), DetailComputerView {

    //global mvalue
    var mId: Int = 0

    //presenter
    private lateinit var presenter: DetailComputerPresenter

    //recyclerview
    private lateinit var historyAdapterB: HistoryAdapter
    private var historyDataB: MutableList<HistoryListData.Result> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_computer)

        presenter = DetailComputerPresenter(this)

        val intent = intent.getParcelableExtra<ComputerListData.Result>(DATA_INTENT_COMPUTER_LIST_DETAIL)

        mId = intent.id
        val mSeatManajemen: String
        val mLokasi: String

        if (intent.seatManagement) {
            mSeatManajemen = "Ya"
        } else {
            mSeatManajemen = "Tidak"
        }

        if (intent.location == "None") {
            mLokasi = "-"
        } else {
            mLokasi = intent.location
        }

        val translate = Translasi()

        tv_detail_client_name.text = intent.clientName.toUpperCase()
        tv_detail_hostname.text = intent.hostname
        tv_detail_ip_address.text = intent.ipAddress
        tv_detail_no_inventory.text = intent.inventoryNumber
        tv_detail_branch.text = intent.branch
        tv_detail_division.text = intent.division
        tv_detail_location.text = mLokasi
        tv_detail_full_pc.text = (intent.computerType + " - " + intent.merkModel + " - " + intent.year).toUpperCase()
        tv_detail_seat_manajemen.text = mSeatManajemen
        tv_detail_os.text = translate.osTranslation(intent.operationSystem)
        tv_detail_prosessor.text = translate.processorTranslation(intent.processor)
        tv_detail_ram.text = translate.ramTranslation(intent.ram)
        tv_detail_hardisk.text = translate.hardiskTranslation(intent.hardisk)
        tv_detail_vga.text = intent.vgaCard
        tv_detail_status.text = intent.status
        tv_detail_note.text = intent.note

        //Historr Recyclerview
        rv_detail_history.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        historyAdapterB = HistoryAdapter(this, historyDataB) {
            startActivity<HistoryDetailActivity>(DATA_INTENT_DASHBOARD_DETAIL_HISTORY to it)
        }
        rv_detail_history.adapter = historyAdapterB

        //mengisi History Recyclerview
        presenter.getHistoryDetail(mId)

        bt_detail_add_history.setOnClickListener {
            startActivity<HistoryAddActivity>(
                INTENT_DETAIL_ADD_HISTORY_ID to intent.id,
                INTENT_DETAIL_ADD_HISTORY_NAME to intent.clientName
            )
        }

        bt_detail_edit_computer.setOnClickListener {
            startActivity<EditComputerActivity>(
                INTENT_DETAIL_EDIT_COMPUTER to intent
            )
        }

        iv_detail_back.setOnClickListener {
            onBackPressed()
        }

        //IF BRANCH TIDAK SAMA DENGAN BRANCH USER TIDAK DAPAT MENAMBAH HISTORY DAN EDIT
        if (App.prefs.userBranchSave != intent.branch){
            bt_detail_add_history.visibility = View.INVISIBLE
            bt_detail_edit_computer.visibility = View.INVISIBLE
        }

    }


    override fun showLoadingHistory() {
        pb_detail_computer_history.visibility = View.VISIBLE
    }

    override fun hideLoadingHistory() {
        pb_detail_computer_history.visibility = View.GONE
    }

    override fun showHistoryList(historyList: List<HistoryListData.Result>) {
        historyDataB.clear()
        historyDataB.addAll(historyList)
        historyAdapterB.notifyDataSetChanged()
    }

    override fun showToastError(notif: String) {
        toast(notif)
    }

    override fun onResume() {
        super.onResume()
        if (Statis.isHistoryUpdate) {
            presenter.getHistoryDetail(mId)
        }
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}
