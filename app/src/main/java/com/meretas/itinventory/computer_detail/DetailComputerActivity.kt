package com.meretas.itinventory.computer_detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.meretas.itinventory.R
import com.meretas.itinventory.dashboard.HistoryAdapter
import com.meretas.itinventory.data.ComputerListData
import com.meretas.itinventory.data.HistoryListData
import com.meretas.itinventory.history.HistoryDetailActivity
import com.meretas.itinventory.utils.DATA_INTENT_COMPUTER_LIST_DETAIL
import com.meretas.itinventory.utils.DATA_INTENT_DASHBOARD_DETAIL_HISTORY
import kotlinx.android.synthetic.main.activity_detail_computer.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class DetailComputerActivity : AppCompatActivity(), DetailComputerView {

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

        val mSeatManajemen: String
        val mOs: String
        val mProsessor: String
        val mLokasi: String

        if (intent.seatManagement) {
            mSeatManajemen = "Ya"
        } else {
            mSeatManajemen = "Tidak"
        }

        if (intent.location == "None"){
            mLokasi = "-"
        } else {
            mLokasi = intent.location
        }

        when (intent.operationSystem) {
            "1064" -> mOs = "Win 10"
            "732" -> mOs = "Win 7 32b"
            "764" -> mOs = "Win 7 64b"
            "864" -> mOs = "Win 8 64b"
            "832" -> mOs = "Win 8 32b"
            "XP" -> mOs = "Win XP"
            "SRV" -> mOs = "Win Server"
            "L" -> mOs = "Linux"
            else -> mOs = "Win 10"
        }

        when (intent.processor) {
            2.0 -> mProsessor = "Kurang dari i3"
            3.0 -> mProsessor = "Setara i3"
            5.0 -> mProsessor = "Setara i5"
            7.0 -> mProsessor = "Setara i7"
            else -> mProsessor = "Setara i3"
        }



        tv_detail_client_name.text = intent.clientName.toUpperCase()
        tv_detail_hostname.text = intent.hostname
        tv_detail_ip_address.text = intent.ipAddress
        tv_detail_no_inventory.text = intent.inventoryNumber
        tv_detail_branch.text = intent.branch
        tv_detail_division.text = intent.division
        tv_detail_location.text = mLokasi
        tv_detail_full_pc.text = (intent.computerType + " - " + intent.merkModel + " - " + intent.year).toUpperCase()
        tv_detail_seat_manajemen.text = mSeatManajemen
        tv_detail_os.text = mOs
        tv_detail_prosessor.text = mProsessor
        tv_detail_ram.text = intent.ram.toString() + " GB"
        tv_detail_hardisk.text = intent.hardisk.toString() + " GB"
        tv_detail_vga.text = intent.vgaCard
        tv_detail_status.text = intent.status
        tv_detail_note.text = intent.note

        //Historr Recyclerview
        rv_detail_history.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        historyAdapterB = HistoryAdapter(this, historyDataB) {
            //startActivity<HistoryDetailActivity>(DATA_INTENT_DASHBOARD_DETAIL_HISTORY to it)
        }
        rv_detail_history.adapter = historyAdapterB

        //mengisi History Recyclerview
        presenter.getHistoryDetail()

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

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}
