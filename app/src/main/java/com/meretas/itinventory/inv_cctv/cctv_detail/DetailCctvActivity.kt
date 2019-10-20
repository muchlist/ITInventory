package com.meretas.itinventory.inv_cctv.cctv_detail

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.meretas.itinventory.R
import com.meretas.itinventory.dashboard.HistoryGeneralAdapter
import com.meretas.itinventory.data.CctvListData
import com.meretas.itinventory.data.HistoryListGeneralData
import com.meretas.itinventory.inv_cctv.cctv_history.AddCctvHistoryActivity
import com.meretas.itinventory.dashboard.HistoryDetailActivity
import com.meretas.itinventory.services.Api
import com.meretas.itinventory.utils.*
import com.meretas.itinventory.utils.Statis.Companion.isCctvHistoryUpdate
import kotlinx.android.synthetic.main.activity_detail_cctv.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class DetailCctvActivity : AppCompatActivity() {

    //View Model
    private lateinit var viewModel: DetailCctvViewModel
    private lateinit var viewModelFactory: DetailCctvViewModelFactory

    //recyclerview
    private lateinit var historyCctvAdapter: HistoryGeneralAdapter
    private var historyCctvData: MutableList<HistoryListGeneralData.Result> = mutableListOf()

    //INTENT
    private lateinit var intentData: CctvListData.Result

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_cctv)

        //Intent dari stock list di transfer ke view model
        intentData = intent.getParcelableExtra(
            DATA_INTENT_CCTV_LIST_TO_DETAIL
        )

        initViewModel()

        viewModelObserve()

        //INJECT DATA DARI INTENT KE VIEWMODEL
        viewModel.injectCctvDataToViewModel(intentData)

        setRecyclerView()
        viewModel.getCctvHistory(App.prefs.authTokenSave, intentData.id)

        //PERCABANGN TOMBOL MENURUT BRANCH
        if (App.prefs.userBranchSave != intentData.branch || App.prefs.isReadOnly) {
            bt_detail_cctv_add_history.disable()
            bt_detail_edit_cctv.disable()
        } else {
            bt_detail_cctv_add_history.enable()
            bt_detail_edit_cctv.enable()

            bt_detail_cctv_add_history.setOnClickListener {
                startActivity<AddCctvHistoryActivity>(
                    INTENT_DETAIL_ADD_HISTORY_CCTV_ID to intentData.id,
                    INTENT_DETAIL_ADD_HISTORY_CCTV_NAME to intentData.cctvName
                )
            }

            bt_detail_edit_cctv.setOnClickListener {
                /*                 startActivity<AddConsumeStockActivity>(
                 INTENT_DETAIL_ADD_CONSUME_ID to intent.id,
                 INTENT_DETAIL_ADD_CONSUME_NAME to intent.stockName
             )*/
            }
        }

        iv_detail_back_cctv.setOnClickListener {
            finish()
        }

    }

    private fun initViewModel() {
        val application = requireNotNull(this).application
        val apiService = Api.retrofitService
        viewModelFactory = DetailCctvViewModelFactory(apiService, application)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(DetailCctvViewModel::class.java)
    }

    private fun setRecyclerView() {
        rv_detail_cctv_history.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        historyCctvAdapter = HistoryGeneralAdapter(this, historyCctvData) {
            startActivity<HistoryDetailActivity>(DATA_INTENT_DASHBOARD_DETAIL_HISTORY to it)
        }
        rv_detail_cctv_history.adapter = historyCctvAdapter
        rv_detail_cctv_history.setHasFixedSize(true)
    }

    private fun viewModelObserve() {
        //LOADING PROGRESS
        viewModel.isLoading.observe(this, Observer {
            if (it) {
                pb_detail_cctv_history.visibility = View.VISIBLE
            } else {
                pb_detail_cctv_history.visibility = View.GONE
            }
        })

        //TOAST
        viewModel.isError.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                toast(it)
            }
        })

        //DETAIL
        viewModel.cctvDetailData.observe(this, Observer {

            tv_detail_cctv_name.text = it.cctvName
            tv_detail_cctv_ip_address.text = it.ipAddress
            tv_detail_cctv_branch.text = it.branch
            tv_detail_cctv_location.text = it.location
            tv_detail_cctv_merk.text = it.merkModel
            tv_detail_cctv_year.text = it.year
            tv_detail_cctv_status.text = it.status
            tv_detail_cctv_note.text = it.note
        })

        viewModel.cctvDetailHistoryCctvData.observe(this, Observer {
            it?.let {
                historyCctvData.clear()
                historyCctvData.addAll(it)
                historyCctvAdapter.notifyDataSetChanged()

                //Declare And SetAnimation
                val topToBottom = AnimationUtils.loadAnimation(this, R.anim.fade_in_history)
                rv_detail_cctv_history.startAnimation(topToBottom)
            }
        })
    }

    override fun onResume() {
        super.onResume()

        if (isCctvHistoryUpdate) {
            viewModel.getCctvHistory(
                token = App.prefs.authTokenSave,
                cctvId = intentData.id
            )
            isCctvHistoryUpdate = false
        }
    }
}
