package com.meretas.itinventory.inv_server

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
import com.meretas.itinventory.data.ServerListData
import com.meretas.itinventory.services.Api
import com.meretas.itinventory.utils.App
import com.meretas.itinventory.utils.Statis
import kotlinx.android.synthetic.main.activity_server_list.*
import kotlinx.coroutines.*
import org.jetbrains.anko.toast

class ServerListActivity : AppCompatActivity() {

    //View Model
    private lateinit var viewModel: ServerListViewModel
    private lateinit var viewModelFactory: ServerListViewModelFactory

    //recyclerView
    private lateinit var serverAdapter: ServerListAdapter
    private var serverData: MutableList<ServerListData.Result> = mutableListOf()

    //JOB search cancelable
    val uiScope = CoroutineScope(Dispatchers.Main)
    private var textChangeCountJob: Job? = null
    private var searchInput: String? = null

    //Dropdown dialog
    private lateinit var myDialog: Dialog
    private lateinit var branchDropdownOption: Array<String>
    private lateinit var statusDropdownOption: Array<String>
    private lateinit var categoryDropdownOption: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_server_list)

        initViewModel()
        resetStaticSaveValue()
        viewModelObserve()
        setRecyclerView()
        requestDataFromInternet()

        initDialogAndDropdown()

        //DIALOG CHIP LISTENER
        chip_serverlist_filter.setOnClickListener {
            showFilterDialog()
        }

        //SEARCH TEXT CHANGE LISTENER
        et_serverlist_searchbar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                textChangeCountJob?.cancel()
                viewModel.requestServerDataSearch(
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
                        viewModel.requestServerDataSearch(
                            token = App.prefs.authTokenSave,
                            search = searchInput!!
                        )
                    }
                }
                return false
            }
        })

        bt_serverlist_tambah.setOnClickListener {
            //startActivity<AddServerActivity>()
        }

        hideKeyboard()

    }

    private fun initViewModel() {
        val application = requireNotNull(this).application
        val apiService = Api.retrofitService
        viewModelFactory =
            ServerListViewModelFactory(
                apiService,
                application
            )
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(ServerListViewModel::class.java)
    }

    private fun initDialogAndDropdown() {
        branchDropdownOption = resources.getStringArray(R.array.branch_array)
        statusDropdownOption = resources.getStringArray(R.array.cctv_status_array)
        categoryDropdownOption = resources.getStringArray(R.array.server_category_array)
        myDialog = Dialog(this)
    }

    private fun setRecyclerView() {
        rv_serverlist.layoutManager = LinearLayoutManager(this)
        serverAdapter =
            ServerListAdapter(this, serverData) {
                //                startActivity<DetailServerActivity>(
//                    DATA_INTENT_SERVER_LIST_TO_DETAIL to it
//                )
            }
        rv_serverlist.adapter = serverAdapter
        rv_serverlist.setHasFixedSize(true)
    }

    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        recyclerView.scheduleLayoutAnimation()
        recyclerView.invalidate()
    }

    private fun requestDataFromInternet() {
        viewModel.requestServerData(
            token = App.prefs.authTokenSave,
            branch = Statis.whatServerBranch,
            category = Statis.whatServerCategory,
            status = Statis.whatServerStatus
        )
    }

    private fun viewModelObserve() {
        // RECYCLERVIEW
        viewModel.getServerData().observe(this, Observer {
            serverData.clear()
            serverData.addAll(it.results)
            runLayoutAnimation(rv_serverlist)
            serverAdapter.notifyDataSetChanged()

            unitCountUpdate(it.count)
        })

        //LOADING PROGRESS
        viewModel.isLoading.observe(this, Observer {
            if (it) {
                pb_serverlist.visibility = View.VISIBLE
            } else {
                pb_serverlist.visibility = View.GONE
            }
        })

        //TOAST
        viewModel.isError.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                toast(it)
            }
        })
    }

    private fun unitCountUpdate(count: Int) {
        tv_serverlist_unit.text = "Jumlah : $count"
    }

    private fun resetStaticSaveValue() {
        Statis.whatServerBranch = App.prefs.userBranchSave
        Statis.whatServerStatus = "Aktif"
        Statis.whatServerCategory = ""
    }

    private fun showFilterDialog() {
        val branchIndexStart = branchDropdownOption.indexOf(Statis.whatServerBranch)
        val categoryIndexStart =
            if (Statis.whatServerCategory.isEmpty()) 0 else categoryDropdownOption.indexOf(Statis.whatServerCategory)
        val statusIndexStart =
            if (Statis.whatServerStatus.isEmpty()) 0 else statusDropdownOption.indexOf(Statis.whatServerStatus)

        var branchSelected = ""
        var statusSelected = ""
        var categorySelected = ""

        myDialog.setContentView(R.layout.dialog_filter_server)

        //BRANCH DROPDOWN
        val branchDropdown: Spinner = myDialog.findViewById(R.id.sp_filter_server_branch)
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

        //CATEGORY DROPDOWN
        val categoryDropdown: Spinner = myDialog.findViewById(R.id.sp_filter_server_category)
        categoryDropdown.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categoryDropdownOption)

        categoryDropdown.setSelection(categoryIndexStart)
        categoryDropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                categorySelected = categoryDropdownOption[position]
            }
        }

        //STATUS DROPDOWN
        val statusDropdown: Spinner = myDialog.findViewById(R.id.sp_filter_server_status)
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
        val buttonServerDialogFilter: Button =
            myDialog.findViewById(R.id.bt_filter_server_apply)
        buttonServerDialogFilter.setOnClickListener {
            Statis.whatServerBranch = branchSelected
            Statis.whatServerStatus =
                if (statusSelected == statusDropdownOption[0]) "" else statusSelected
            Statis.whatServerCategory =
                if (categorySelected == categoryDropdownOption[0]) "" else categorySelected

            viewModel.requestServerData(
                token = App.prefs.authTokenSave,
                branch = Statis.whatServerBranch,
                category = Statis.whatServerCategory,
                status = Statis.whatServerStatus
            )
            myDialog.dismiss()
        }
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog.show()
    }

    private fun hideKeyboard() {
        et_serverlist_searchbar.isFocusable = false
        et_serverlist_searchbar.clearFocus()
    }

    override fun onResume() {
        super.onResume()
        if (Statis.isServerUpdate) {
            requestDataFromInternet()
        }
        Statis.isServerUpdate = false
    }

    override fun onStop() {
        textChangeCountJob?.cancel()
        super.onStop()
    }
}