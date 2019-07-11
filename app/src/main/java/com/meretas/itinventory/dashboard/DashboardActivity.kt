package com.meretas.itinventory.dashboard

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.meretas.itinventory.R
import com.meretas.itinventory.computer_list.ComputerListActivity
import com.meretas.itinventory.data.HistoryListData
import com.meretas.itinventory.history.HistoryDetailActivity
import com.meretas.itinventory.utils.App
import com.meretas.itinventory.utils.DATA_INTENT_DASHBOARD_COMPUTER_LIST
import com.meretas.itinventory.utils.DATA_INTENT_DASHBOARD_DETAIL_HISTORY
import com.meretas.itinventory.utils.Statis
import kotlinx.android.synthetic.main.activity_dashboard.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class DashboardActivity : AppCompatActivity(), DashboarView {

    //presenter
    private lateinit var presenter: DashboardPresenter

    //recycler view
    private lateinit var historyAdapter: HistoryAdapter
    private var historyData: MutableList<HistoryListData.Result> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        presenter = DashboardPresenter(this)

        //update info user
        if (App.prefs.userBranchSave.isNotEmpty()) {
            toolbar_dashboard.title = App.prefs.userNameSave
            toolbar_dashboard.subtitle = App.prefs.userBranchSave
        }
        presenter.getCurrentUserInfo()


        //Historr Recyclerview
        rv_history_dashboard.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        historyAdapter = HistoryAdapter(this, historyData) {
            startActivity<HistoryDetailActivity>(DATA_INTENT_DASHBOARD_DETAIL_HISTORY to it)
        }
        rv_history_dashboard.adapter = historyAdapter


        //mengisi History Recyclerview
        presenter.getHistoryDashboard()

        //mGvKategori.layoutAnimation = controller


        //onclick listener menu
        cv_dashboard_inventory.setOnClickListener {
            startActivity<ComputerListActivity>(DATA_INTENT_DASHBOARD_COMPUTER_LIST to "")
        }

        cv_dashboard_consumable.setOnClickListener {
            showToast("Menu Consumable Item Belum Tersedia")
        }
        cv_dashboard_other.setOnClickListener {
            showToast("Menu Other Item Belum Tersedia")
        }

        et_dashboard_searchbar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                startActivity<ComputerListActivity>(DATA_INTENT_DASHBOARD_COMPUTER_LIST to query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        //Load Button
        //Declare Animation
        val bottomToTop = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top)
        val scaleToOne = AnimationUtils.loadAnimation(this, R.anim.scale_to_one)
        val scaleToTwo = AnimationUtils.loadAnimation(this, R.anim.scale_to_two)
        val scaleToThree = AnimationUtils.loadAnimation(this, R.anim.scale_to_three)
        //SetAnimation
        bt_dashboard_reload.startAnimation(bottomToTop)
        cv_dashboard_inventory.startAnimation(scaleToOne)
        cv_dashboard_consumable.startAnimation(scaleToTwo)
        cv_dashboard_other.startAnimation(scaleToThree)

        //HIDE KEYBOARD
        et_dashboard_searchbar.setFocusable(false)
        et_dashboard_searchbar.clearFocus()

    }

    override fun getUserInfo(name: String, branch: String, isReadOnly: Boolean) {
        toolbar_dashboard.title = name
        toolbar_dashboard.subtitle = branch
        App.prefs.userNameSave = name
        App.prefs.userBranchSave = branch
        App.prefs.isReadOnly = isReadOnly
    }

    override fun showHistory(data: List<HistoryListData.Result>) {
        historyData.clear()
        historyData.addAll(data)
        historyAdapter.notifyDataSetChanged()

        //Declare Animation
        val topToBottom = AnimationUtils.loadAnimation(this, R.anim.fade_in_history)
        //SetAnimation
        rv_history_dashboard.startAnimation(topToBottom)
    }

    override fun hideProgressBarHistory() {
        pb_history_dashboard.visibility = View.GONE
    }

    override fun showProgressBarHistory() {
        pb_history_dashboard.visibility = View.VISIBLE
    }

    override fun showToast(notif: String) {
        toast(notif)
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        if (Statis.isHistoryUpdate) {
            presenter.getHistoryDashboard()
            Statis.isHistoryUpdate = false
        }
    }
}
