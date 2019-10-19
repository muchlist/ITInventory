package com.meretas.itinventory.inv_cctv.cctv_list

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.meretas.itinventory.R
import com.meretas.itinventory.data.CctvListData
import com.meretas.itinventory.inv_cctv.cctv_add.AddCctvActivity
import com.meretas.itinventory.services.Api
import com.meretas.itinventory.utils.App
import com.meretas.itinventory.utils.Statis.Companion.isCctvUpdate
import com.meretas.itinventory.utils.Statis.Companion.whatCctvBranch
import com.meretas.itinventory.utils.Statis.Companion.whatCctvStatus
import kotlinx.android.synthetic.main.activity_cctv_list.*
import kotlinx.coroutines.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class CctvListActivity : AppCompatActivity() {

    //View Model
    private lateinit var viewModel: CctvListViewModel
    private lateinit var viewModelFactory: CctvListViewModelFactory

    //recyclerView
    private lateinit var cctvAdapter: CctvListAdapter
    private var cctvData: MutableList<CctvListData.Result> = mutableListOf()

    //JOB search cancelable
    val uiScope = CoroutineScope(Dispatchers.Main)
    private var textChangeCountJob: Job? = null
    private var searchInput: String? = null

    //Dropdown dialog
    private lateinit var myDialog: Dialog
    private lateinit var branchDropdownOption: Array<String>
    private lateinit var statusDropdownOption: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cctv_list)

        initViewModel()
        resetStaticSaveValue()
        viewModelObserve()
        setRecyclerView()
        requestDataFromInternet()

        initDialogAndDropdown()

        //DIALOG CHIP LISTENER
        chip_cctvlist_filter.setOnClickListener {
            showFilterDialog()
        }

        //SEARCH TEXT CHANGE LISTENER
        et_cctvlist_searchbar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                textChangeCountJob?.cancel()
                viewModel.requestCctvDataSearch(
                    token = App.prefs.authTokenSave,
                    search = query!!
                )
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                searchInput = newText!!
                textChangeCountJob?.cancel()

                if (searchInput!!.length > 2) {
                    textChangeCountJob = uiScope.launch {
                        delay(800L)
                        viewModel.requestCctvDataSearch(
                            token = App.prefs.authTokenSave,
                            search = searchInput!!
                        )
                    }
                }
                return false
            }
        })

        bt_cctvlist_tambah.setOnClickListener { startActivity<AddCctvActivity>() }

        hideKeyboard()

    }

    private fun initViewModel() {
        val application = requireNotNull(this).application
        val apiService = Api.retrofitService
        viewModelFactory = CctvListViewModelFactory(apiService, application)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(CctvListViewModel::class.java)
    }

    private fun initDialogAndDropdown() {
        branchDropdownOption = resources.getStringArray(R.array.branch_array)
        statusDropdownOption = resources.getStringArray(R.array.cctv_status_array)
        myDialog = Dialog(this)
    }

    private fun setRecyclerView() {
        rv_cctvlist.layoutManager = LinearLayoutManager(this)
        cctvAdapter = CctvListAdapter(this, cctvData) {
            /*startActivity<DetailStockActivity>(
                DATA_INTENT_STOCK_LIST_DETAIL to it
            )*/
        }
        rv_cctvlist.adapter = cctvAdapter
        rv_cctvlist.setHasFixedSize(true)
    }

    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        recyclerView.scheduleLayoutAnimation()
        recyclerView.invalidate()
    }

    private fun requestDataFromInternet() {
        viewModel.requestCctvData(
            token = App.prefs.authTokenSave,
            branch = whatCctvBranch,
            status = whatCctvStatus
        )
    }

    private fun viewModelObserve() {
        // RECYCLERVIEW
        viewModel.getCctvData().observe(this, Observer {
            cctvData.clear()
            cctvData.addAll(it.results)
            runLayoutAnimation(rv_cctvlist)
            cctvAdapter.notifyDataSetChanged()

            totalUnitUpdate(it.count)
        })

        //LOADING PROGRESS
        viewModel.isLoading.observe(this, Observer {
            if (it) {
                pb_cctvlist.visibility = View.VISIBLE
            } else {
                pb_cctvlist.visibility = View.GONE
            }
        })

        //TOAST
        viewModel.isError.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                toast(it)
            }
        })
    }

    private fun totalUnitUpdate(count: Int) {
        tv_cctvlist_unit.text = "Jumlah : $count"
    }

    private fun resetStaticSaveValue() {
        whatCctvBranch = App.prefs.userBranchSave
        whatCctvStatus = "Aktif"
    }

    private fun showFilterDialog() {
        val branchIndexStart = branchDropdownOption.indexOf(whatCctvBranch)
        val statusIndexStart =
            if (whatCctvStatus.isEmpty()) 0 else statusDropdownOption.indexOf(whatCctvStatus)

        var branchSelected = ""
        var statusSelected = ""

        myDialog.setContentView(R.layout.dialog_filter_cctv)

        //BRANCH DROPDOWN
        val branchDropdown: Spinner = myDialog.findViewById(R.id.sp_filter_cctv_branch)
        branchDropdown.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, branchDropdownOption)

        branchDropdown.setSelection(branchIndexStart)
        branchDropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                branchSelected = branchDropdownOption[position]
            }

        }

        //STATUS DROPDOWN
        val statusDropdown: Spinner = myDialog.findViewById(R.id.sp_filter_cctv_status)
        statusDropdown.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, statusDropdownOption)

        statusDropdown.setSelection(statusIndexStart)
        statusDropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                statusSelected = statusDropdownOption[position]
            }
        }

        //TOMBOL APPLY
        val buttonCctvDialogFilter: Button =
            myDialog.findViewById(R.id.bt_filter_cctv_apply)
        buttonCctvDialogFilter.setOnClickListener {
            whatCctvBranch = branchSelected
            whatCctvStatus =
                if (statusSelected == statusDropdownOption[0]) "" else statusSelected

            viewModel.requestCctvData(
                token = App.prefs.authTokenSave,
                branch = whatCctvBranch,
                status = whatCctvStatus
            )
            myDialog.dismiss()
        }
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog.show()
    }

    private fun hideKeyboard() {
        et_cctvlist_searchbar.isFocusable = false
        et_cctvlist_searchbar.clearFocus()
    }

    override fun onResume() {
        super.onResume()
        if (isCctvUpdate) {
            requestDataFromInternet()
        }
        isCctvUpdate = false
    }

    override fun onStop() {
        textChangeCountJob?.cancel()
        super.onStop()
    }
}
