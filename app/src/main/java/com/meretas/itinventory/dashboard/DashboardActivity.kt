package com.meretas.itinventory.dashboard

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.meretas.itinventory.computer_list.ComputerListActivity
import com.meretas.itinventory.data.HistoryListData
import com.meretas.itinventory.history.HistoryDetailActivity
import com.meretas.itinventory.utils.App
import com.meretas.itinventory.utils.DATA_INTENT_DASHBOARD_COMPUTER_LIST
import com.meretas.itinventory.utils.DATA_INTENT_DASHBOARD_DETAIL_HISTORY
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
        setContentView(com.meretas.itinventory.R.layout.activity_dashboard)

        presenter = DashboardPresenter(this)

        //update info user
        if (App.prefs.userBranchSave.isNotEmpty()){
            toolbar_dashboard.title = App.prefs.userNameSave
            toolbar_dashboard.subtitle = "Cabang "+ App.prefs.userBranchSave
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


        //onclick listener menu
        cv_dashboard_inventory.setOnClickListener {
            startActivity<ComputerListActivity>(DATA_INTENT_DASHBOARD_COMPUTER_LIST to "")
        }

        cv_dashboard_consumable.setOnClickListener{
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

        //HIDE KEYBOARD
        et_dashboard_searchbar.setFocusable(false)
        et_dashboard_searchbar.clearFocus()

    }

    override fun getUserInfo(name: String, branch: String) {
        toolbar_dashboard.title = name
        toolbar_dashboard.subtitle = "Cabang " + branch
        App.prefs.userNameSave = name
        App.prefs.userBranchSave = branch
    }

    override fun showHistory(data: List<HistoryListData.Result>) {
        historyData.clear()
        historyData.addAll(data)
        historyAdapter.notifyDataSetChanged()
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
}
