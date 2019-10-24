package com.meretas.itinventory.inv_server.server_detail

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
import com.meretas.itinventory.data.ServerListData
import com.meretas.itinventory.inv_server.server_history.AddServerHistoryActivity
import com.meretas.itinventory.services.Api
import com.meretas.itinventory.utils.*
import com.meretas.itinventory.utils.Statis.Companion.isHistoryUpdate
import com.meretas.itinventory.utils.Statis.Companion.isServerUpdate
import kotlinx.android.synthetic.main.activity_detail_server.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class DetailServerActivity : AppCompatActivity() {

    //View Model
    private lateinit var viewModel: DetailServerViewModel
    private lateinit var viewModelFactory: DetailServerViewModelFactory

    //recyclerview
    private lateinit var historyServerAdapter: HistoryGeneralAdapter
    private var historyServerData: MutableList<HistoryListGeneralData.Result> = mutableListOf()

    //INTENT
    private lateinit var intentData: ServerListData.Result

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_server)
        //Intent dari stock list di transfer ke view model
        intentData = intent.getParcelableExtra(
            DATA_INTENT_SERVER_LIST_TO_DETAIL
        )

        initViewModel()

        viewModelObserve()

        //INJECT DATA DARI INTENT KE VIEWMODEL
        viewModel.injectServerDataToViewModel(intentData)

        setRecyclerView()
        viewModel.getServerHistory(App.prefs.authTokenSave, intentData.id)

        //PERCABANGN TOMBOL MENURUT BRANCH
        if (App.prefs.userBranchSave != intentData.branch || App.prefs.isReadOnly) {
            bt_detail_server_add_history.disable()
            bt_detail_edit_server.disable()
        } else {
            bt_detail_server_add_history.enable()
            bt_detail_edit_server.enable()

            bt_detail_server_add_history.setOnClickListener {
                startActivity<AddServerHistoryActivity>(
                    INTENT_DETAIL_ADD_HISTORY_SERVER_ID to intentData.id,
                    INTENT_DETAIL_ADD_HISTORY_SERVER_NAME to intentData.serverName
                )
            }

            bt_detail_edit_server.setOnClickListener {
//                startActivity<EditServerActivity>(
//                    DATA_INTENT_SERVER_DETAIL to viewModel.serverDetailData.value
//                )
            }
        }

        iv_detail_back_server.setOnClickListener {
            finish()
        }

    }

    private fun initViewModel() {
        val application = requireNotNull(this).application
        val apiService = Api.retrofitService
        viewModelFactory = DetailServerViewModelFactory(apiService, application)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(DetailServerViewModel::class.java)
    }

    private fun setRecyclerView() {
        rv_detail_server_history.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        historyServerAdapter = HistoryGeneralAdapter(this, historyServerData) {
            startActivity<HistoryDetailActivity>(DATA_INTENT_DASHBOARD_DETAIL_HISTORY to it)
        }
        rv_detail_server_history.adapter = historyServerAdapter
        rv_detail_server_history.setHasFixedSize(true)
    }

    private fun viewModelObserve() {
        //LOADING PROGRESS
        viewModel.isLoading.observe(this, Observer {
            if (it) {
                pb_detail_server_history.visibility = View.VISIBLE
            } else {
                pb_detail_server_history.visibility = View.GONE
            }
        })

        //TOAST
        viewModel.isError.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                toast(it)
            }
        })

        //DETAIL
        viewModel.serverDetailData.observe(this, Observer {
            tv_detail_server_name.text = it.serverName
            tv_detail_server_ip_address.text = it.ipAddress
            tv_detail_server_branch.text = it.branch
            tv_detail_server_category.text = it.category
            tv_detail_server_merk.text = it.merkModel
            tv_detail_server_year.text = it.year
            tv_detail_server_status.text = it.status
            tv_detail_server_location.text = it.location
            tv_detail_server_note.text = it.note
        })

        viewModel.serverDetailHistoryServerData.observe(this, Observer {
            it?.let {
                historyServerData.clear()
                historyServerData.addAll(it)
                historyServerAdapter.notifyDataSetChanged()

                //Declare And SetAnimation
                val topToBottom = AnimationUtils.loadAnimation(this, R.anim.fade_in_history)
                rv_detail_server_history.startAnimation(topToBottom)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (isServerUpdate) {
            viewModel.getServerRefresh(App.prefs.authTokenSave, intentData.id)
        }

        if (isHistoryUpdate) {
            viewModel.getServerHistory(
                token = App.prefs.authTokenSave,
                serverId = intentData.id
            )
        }
    }
}
