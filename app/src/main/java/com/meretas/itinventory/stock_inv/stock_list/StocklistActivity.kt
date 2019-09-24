package com.meretas.itinventory.stock_inv.stock_list

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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.meretas.itinventory.R
import com.meretas.itinventory.data.StockListData
import com.meretas.itinventory.stock_inv.stock_add.AddStockActivity
import com.meretas.itinventory.stock_inv.stock_detail.DetailStockActivity
import com.meretas.itinventory.utils.App
import com.meretas.itinventory.utils.DATA_INTENT_STOCK_LIST_DETAIL
import com.meretas.itinventory.utils.Statis.Companion.isStockChangeMinus
import com.meretas.itinventory.utils.Statis.Companion.isStockChangePlus
import com.meretas.itinventory.utils.Statis.Companion.isStockUpdate
import com.meretas.itinventory.utils.Statis.Companion.whatStockActiveStatus
import com.meretas.itinventory.utils.Statis.Companion.whatStockBranch
import com.meretas.itinventory.utils.Statis.Companion.whatStockCategory
import kotlinx.android.synthetic.main.activity_list_stock.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class StocklistActivity : AppCompatActivity() {

    private lateinit var viewModel: StocklistViewModel
    private lateinit var stockAdapter: StocklistAdapter
    private var stockData: MutableList<StockListData.Result> = mutableListOf()

    private lateinit var myDialog: Dialog
    private lateinit var branchDropdownOption: Array<String>
    private lateinit var statusDropdownOption: Array<String>
    private lateinit var categoryDropdownOption: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_stock)

        //INIT VIEW MODEL
        viewModel = ViewModelProviders.of(this).get(StocklistViewModel::class.java)

        //INIT OPTION Array from string.xml
        branchDropdownOption = resources.getStringArray(R.array.branch_array)
        statusDropdownOption = resources.getStringArray(R.array.stock_active_array)
        categoryDropdownOption = resources.getStringArray(R.array.stock_category_array)

        //RESET VALUE
        resetStaticValue()

        // DATA RECYCLERVIEW
        viewModel.stockData.observe(this, Observer {
            stockData.clear()
            stockData.addAll(it.results)
            runLayoutAnimation(rv_stocklist)
            stockAdapter.notifyDataSetChanged()
        })

        setRecyclerView()

        //MERUBAH DATA RECYCLER VIEW #1
        viewModel.getStockData(App.prefs.userBranchSave, whatStockCategory, whatStockActiveStatus)

        //BUTTON ADD
        bt_stocklist_tambah.setOnClickListener {
            if (App.prefs.userBranchSave == "ReadOnly" || App.prefs.isReadOnly || App.prefs.userBranchSave.isEmpty()) {
                toast("User Tidak Dapat Menambahkan Stok")
            } else {
                startActivity<AddStockActivity>()
            }

        }


        //LOADING
        viewModel.isLoading.observe(this, Observer {
            if (it) {
                pb_stocklist.visibility = View.VISIBLE
            } else {
                pb_stocklist.visibility = View.GONE
            }
        })

        //ERROR
        viewModel.isError.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                toast(it)
            }
        })

        //INIT dialog
        myDialog = Dialog(this)

        //BUTTON FILTER
        chip_stock_filter.setOnClickListener {
            showFilterDialog()
        }

    }

    private fun setRecyclerView() {
        rv_stocklist.layoutManager = LinearLayoutManager(this)
        stockAdapter = StocklistAdapter(this, stockData) {
            startActivity<DetailStockActivity>(
                DATA_INTENT_STOCK_LIST_DETAIL to it
            )
        }
        rv_stocklist.adapter = stockAdapter
        rv_stocklist.setHasFixedSize(true)
    }

    private fun showFilterDialog() {
        val branchIndexStart = branchDropdownOption.indexOf(whatStockBranch)
        val activeIndexStart = if (whatStockActiveStatus) 0 else 1 //0 aktif 1 nonaktif
        val categoryIndexStart =
            if (whatStockCategory.isEmpty()) 0 else categoryDropdownOption.indexOf(whatStockCategory)

        var branchSelected = ""
        var statusSelected = ""
        var categorySelected = ""

        myDialog.setContentView(R.layout.dialog_filter_stock)

        //BRANCH DROPDOWN
        val branchDropdown: Spinner = myDialog.findViewById(R.id.sp_filter_stock_branch)
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
        val statusDropdown: Spinner = myDialog.findViewById(R.id.sp_filter_stock_status)
        statusDropdown.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, statusDropdownOption)

        statusDropdown.setSelection(activeIndexStart)
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

        //CATEGORY DROPDOWN
        val categoryDropdown: Spinner = myDialog.findViewById(R.id.sp_filter_stock_category)
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

        val buttonStockDialogFilter: Button = myDialog.findViewById(R.id.bt_filter_stock_apply)
        buttonStockDialogFilter.setOnClickListener {
            whatStockBranch = branchSelected
            whatStockCategory =
                if (categorySelected == categoryDropdownOption[0]) "" else categorySelected
            whatStockActiveStatus = statusSelected == statusDropdownOption[0]

            viewModel.getStockData(
                whatStockBranch,
                whatStockCategory,
                whatStockActiveStatus
            )
            myDialog.dismiss()
        }

        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog.show()
    }

    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        recyclerView.scheduleLayoutAnimation()
        recyclerView.invalidate()
    }

    private fun resetStaticValue() {
        whatStockCategory = ""
        whatStockActiveStatus = true
        whatStockBranch = App.prefs.userBranchSave
    }

    override fun onResume() {
        super.onResume()
        //JIKA STOCK UPDATE TRUE < HANYA BISA TRUE JIKA ADA PENAMBAHAN STOCK
        if (isStockUpdate || isStockChangeMinus || isStockChangePlus) {
            viewModel.getStockData(
                whatStockBranch,
                whatStockCategory,
                whatStockActiveStatus
            )
            isStockUpdate = false
            isStockChangeMinus = false
            isStockChangePlus = false
        }
    }

}
