package com.meretas.itinventory.inv_printer.printer_list

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
import com.meretas.itinventory.data.PrinterListData
import com.meretas.itinventory.inv_printer.printer_add.AddPrinterActivity
import com.meretas.itinventory.services.Api
import com.meretas.itinventory.utils.App
import com.meretas.itinventory.utils.Statis
import kotlinx.android.synthetic.main.activity_printer_list.*
import kotlinx.coroutines.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class PrinterListActivity : AppCompatActivity() {

    //View Model
    private lateinit var viewModel: PrinterListViewModel
    private lateinit var viewModelFactory: PrinterListViewModelFactory

    //recyclerView
    private lateinit var printerAdapter: PrinterListAdapter
    private var printerData: MutableList<PrinterListData.Result> = mutableListOf()

    //JOB search cancelable
    val uiScope = CoroutineScope(Dispatchers.Main)
    private var textChangeCountJob: Job? = null
    private var searchInput: String? = null

    //Dropdown dialog
    private lateinit var myDialog: Dialog
    private lateinit var branchDropdownOption: Array<String>
    private lateinit var statusDropdownOption: Array<String>
    private lateinit var divisionDropdownOption: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_printer_list)

        initViewModel()
        resetStaticSaveValue()
        viewModelObserve()
        setRecyclerView()
        requestDataFromInternet()

        initDialogAndDropdown()

        //DIALOG CHIP LISTENER
        chip_printerlist_filter.setOnClickListener {
            showFilterDialog()
        }

        //SEARCH TEXT CHANGE LISTENER
        et_printerlist_searchbar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                textChangeCountJob?.cancel()
                viewModel.requestPrinterDataSearch(
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
                        viewModel.requestPrinterDataSearch(
                            token = App.prefs.authTokenSave,
                            search = searchInput!!
                        )
                    }
                }
                return false
            }
        })

        bt_printerlist_tambah.setOnClickListener {
            startActivity<AddPrinterActivity>()
        }

        hideKeyboard()

    }

    private fun initViewModel() {
        val application = requireNotNull(this).application
        val apiService = Api.retrofitService
        viewModelFactory =
            PrinterListViewModelFactory(
                apiService,
                application
            )
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(PrinterListViewModel::class.java)
    }

    private fun initDialogAndDropdown() {
        branchDropdownOption = resources.getStringArray(R.array.branch_array)
        statusDropdownOption = resources.getStringArray(R.array.printer_status_array)
        divisionDropdownOption = resources.getStringArray(R.array.computer_division_array)
        myDialog = Dialog(this)
    }

    private fun setRecyclerView() {
        rv_printerlist.layoutManager = LinearLayoutManager(this)
        printerAdapter =
            PrinterListAdapter(this, printerData) {
                //            startActivity<DetailPrinterActivity>(
//                DATA_INTENT_CCTV_LIST_TO_DETAIL to it
//            )
            }
        rv_printerlist.adapter = printerAdapter
        rv_printerlist.setHasFixedSize(true)
    }

    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        recyclerView.scheduleLayoutAnimation()
        recyclerView.invalidate()
    }

    private fun requestDataFromInternet() {
        viewModel.requestPrinterData(
            token = App.prefs.authTokenSave,
            branch = Statis.whatPrinterBranch,
            division = Statis.whatPrinterDivision,
            status = Statis.whatPrinterStatus
        )
    }

    private fun viewModelObserve() {
        // RECYCLERVIEW
        viewModel.getPrinterData().observe(this, Observer {
            printerData.clear()
            printerData.addAll(it.results)
            runLayoutAnimation(rv_printerlist)
            printerAdapter.notifyDataSetChanged()

            unitCountUpdate(it.count)
        })

        //LOADING PROGRESS
        viewModel.isLoading.observe(this, Observer {
            if (it) {
                pb_printerlist.visibility = View.VISIBLE
            } else {
                pb_printerlist.visibility = View.GONE
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
        tv_printerlist_unit.text = "Jumlah : $count"
    }

    private fun resetStaticSaveValue() {
        Statis.whatPrinterBranch = App.prefs.userBranchSave
        Statis.whatPrinterStatus = "Baik"
        Statis.whatPrinterDivision = ""
    }

    private fun showFilterDialog() {
        val branchIndexStart = branchDropdownOption.indexOf(Statis.whatPrinterBranch)
        val divisionIndexStart =
            if (Statis.whatPrinterDivision.isEmpty()) 0 else divisionDropdownOption.indexOf(Statis.whatPrinterDivision)
        val statusIndexStart =
            if (Statis.whatPrinterStatus.isEmpty()) 0 else statusDropdownOption.indexOf(Statis.whatPrinterStatus)

        var branchSelected = ""
        var statusSelected = ""
        var divisionSelected = ""

        myDialog.setContentView(R.layout.dialog_filter_printer)

        //BRANCH DROPDOWN
        val branchDropdown: Spinner = myDialog.findViewById(R.id.sp_filter_printer_branch)
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

        //DIVISION DROPDOWN
        val divisionDropdown: Spinner = myDialog.findViewById(R.id.sp_filter_printer_division)
        divisionDropdown.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, divisionDropdownOption)

        divisionDropdown.setSelection(divisionIndexStart)
        divisionDropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                divisionSelected = divisionDropdownOption[position]
            }
        }

        //STATUS DROPDOWN
        val statusDropdown: Spinner = myDialog.findViewById(R.id.sp_filter_printer_status)
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
        val buttonPrinterDialogFilter: Button =
            myDialog.findViewById(R.id.bt_filter_printer_apply)
        buttonPrinterDialogFilter.setOnClickListener {
            Statis.whatPrinterBranch = branchSelected
            Statis.whatPrinterStatus =
                if (statusSelected == statusDropdownOption[0]) "" else statusSelected
            Statis.whatPrinterDivision =
                if (divisionSelected == divisionDropdownOption[0]) "" else divisionSelected

            viewModel.requestPrinterData(
                token = App.prefs.authTokenSave,
                branch = Statis.whatPrinterBranch,
                division = Statis.whatPrinterDivision,
                status = Statis.whatPrinterStatus
            )
            myDialog.dismiss()
        }
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog.show()
    }

    private fun hideKeyboard() {
        et_printerlist_searchbar.isFocusable = false
        et_printerlist_searchbar.clearFocus()
    }

    override fun onResume() {
        super.onResume()
        if (Statis.isPrinterUpdate) {
            requestDataFromInternet()
        }
        Statis.isPrinterUpdate = false
    }

    override fun onStop() {
        textChangeCountJob?.cancel()
        super.onStop()
    }
}
