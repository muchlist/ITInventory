package com.meretas.itinventory.inv_computer.computer_detail

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.meretas.itinventory.R
import com.meretas.itinventory.dashboard.HistoryDetailActivity
import com.meretas.itinventory.dashboard.HistoryGeneralAdapter
import com.meretas.itinventory.data.ComputerListData
import com.meretas.itinventory.data.ComputerPostData
import com.meretas.itinventory.data.HistoryListGeneralData
import com.meretas.itinventory.inv_computer.computer_add_history.AddHistoryComputerActivity
import com.meretas.itinventory.inv_computer.computer_edit.EditComputerActivity
import com.meretas.itinventory.utils.*
import kotlinx.android.synthetic.main.activity_detail_computer.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast

class DetailComputerActivity : AppCompatActivity(), DetailComputerView {

    //global mvalue
    private var mId: Int = 0
    private lateinit var translate: Translasi

    //presenter
    private lateinit var presenter: DetailComputerPresenter

    //recyclerview
    private lateinit var historyGeneralAdapterB: HistoryGeneralAdapter
    private var historyGeneralDataB: MutableList<HistoryListGeneralData.Result> = mutableListOf()

    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_computer)

        presenter = DetailComputerPresenter(this)

        val intent =
            intent.getParcelableExtra<ComputerListData.Result>(DATA_INTENT_COMPUTER_LIST_DETAIL)

        mId = intent.id
        val mSeatManajemen: String
        val mLokasi: String

        mSeatManajemen = if (intent.seatManagement) {
            "Ya"
        } else {
            "Tidak"
        }

        mLokasi = if (intent.location == "None") {
            "-"
        } else {
            intent.location
        }

        translate = Translasi()

        tv_detail_client_name.text = intent.clientName.toUpperCase()
        tv_detail_hostname.text = intent.hostname
        tv_detail_ip_address.text = intent.ipAddress
        tv_detail_no_inventory.text = intent.inventoryNumber
        tv_detail_branch.text = intent.branch
        tv_detail_division.text = intent.division
        tv_detail_location.text = mLokasi
        tv_detail_full_pc.text =
            (intent.computerType + " - " + intent.merkModel + " - " + intent.year).toUpperCase()
        tv_detail_seat_manajemen.text = mSeatManajemen
        tv_detail_os.text = translate.osTranslation(intent.operationSystem)
        tv_detail_prosessor.text = translate.processorTranslation(intent.processor)
        tv_detail_ram.text = translate.ramTranslation(intent.ram)
        tv_detail_hardisk.text = translate.hardiskTranslation(intent.hardisk)
        tv_detail_vga.text = intent.vgaCard
        tv_detail_status.text = intent.status
        tv_detail_note.text = intent.note

        //Historr Recyclerview
        rv_detail_history.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        historyGeneralAdapterB = HistoryGeneralAdapter(this, historyGeneralDataB) {
            startActivity<HistoryDetailActivity>(DATA_INTENT_DASHBOARD_DETAIL_HISTORY to it)
        }
        rv_detail_history.adapter = historyGeneralAdapterB

        //mengisi History Recyclerview
        presenter.getHistoryDetail(mId)

        bt_detail_add_history.setOnClickListener {
            startActivity<AddHistoryComputerActivity>(
                INTENT_DETAIL_ADD_HISTORY_ID to intent.id,
                INTENT_DETAIL_ADD_HISTORY_NAME to intent.clientName
            )
        }

        bt_detail_edit_computer.setOnClickListener {
            startActivityForResult<EditComputerActivity>(
                1,
                INTENT_DETAIL_EDIT_COMPUTER to intent
            )
        }

        iv_detail_back.setOnClickListener {
            onBackPressed()
        }

        //IF BRANCH TIDAK SAMA DENGAN BRANCH USER TIDAK DAPAT MENAMBAH HISTORY DAN EDIT
        if (App.prefs.userBranchSave != intent.branch || App.prefs.isReadOnly) {
            bt_detail_add_history.disable()
            bt_detail_edit_computer.disable()
        }
    }


    override fun showLoadingHistory() {
        pb_detail_computer_history.visibility = View.VISIBLE
    }

    override fun hideLoadingHistory() {
        pb_detail_computer_history.visibility = View.GONE
    }

    override fun showHistoryList(historyListGeneral: List<HistoryListGeneralData.Result>) {
        historyGeneralDataB.clear()
        historyGeneralDataB.addAll(historyListGeneral)
        historyGeneralAdapterB.notifyDataSetChanged()

        //Declare And SetAnimation
        val topToBottom = AnimationUtils.loadAnimation(this, R.anim.fade_in_history)
        rv_detail_history.startAnimation(topToBottom)
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

    @SuppressLint("DefaultLocale")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                bt_detail_edit_computer.isClickable = false

                val dataResult: ComputerPostData =
                    data!!.getParcelableExtra(INTENT_EDIT_COMPUTER_RESULT)
                tv_detail_client_name.text = dataResult.namaUser.toUpperCase()
                tv_detail_hostname.text = dataResult.hostKomputer
                tv_detail_ip_address.text = dataResult.alamatIp
                tv_detail_no_inventory.text = dataResult.nomerInventaris
                tv_detail_branch.text = App.prefs.userBranchSave
                tv_detail_division.text = dataResult.divisi
                tv_detail_location.text = dataResult.lokasi
                tv_detail_full_pc.text =
                    (dataResult.jenisPerangkat + " - " + dataResult.merkPerangkat + " - " + dataResult.tahun).toUpperCase()
                tv_detail_seat_manajemen.text = dataResult.seatManajement.toString()
                tv_detail_os.text = translate.osTranslation(dataResult.sistemOperasi ?: "1064")
                tv_detail_prosessor.text = translate.processorTranslation(dataResult.processor)
                tv_detail_ram.text = translate.ramTranslation(dataResult.ram)
                tv_detail_hardisk.text = translate.hardiskTranslation(dataResult.hardisk)
                tv_detail_vga.text = dataResult.vga
                tv_detail_status.text = dataResult.statusPC
                tv_detail_note.text = dataResult.note
            }
        }
    }
}
