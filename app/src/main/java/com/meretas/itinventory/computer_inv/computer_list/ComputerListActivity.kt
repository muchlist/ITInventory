package com.meretas.itinventory.computer_inv.computer_list

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.meretas.itinventory.R
import com.meretas.itinventory.computer_inv.add_computer.AddComputerActivity
import com.meretas.itinventory.computer_inv.computer_detail.DetailComputerActivity
import com.meretas.itinventory.data.ComputerListData
import com.meretas.itinventory.utils.App
import com.meretas.itinventory.utils.DATA_INTENT_COMPUTER_LIST_DETAIL
import com.meretas.itinventory.utils.DATA_INTENT_DASHBOARD_COMPUTER_LIST
import com.meretas.itinventory.utils.Statis
import com.meretas.itinventory.utils.Statis.Companion.whatComputerBranch
import com.meretas.itinventory.utils.Statis.Companion.whatComputerDivision
import com.meretas.itinventory.utils.Statis.Companion.whatComputerLocation
import com.meretas.itinventory.utils.Statis.Companion.whatComputerSeatManajemen
import com.meretas.itinventory.utils.Statis.Companion.whatComputerStatus
import kotlinx.android.synthetic.main.activity_computer_list.*
import kotlinx.coroutines.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class ComputerListActivity : AppCompatActivity(), ComputerListView {

    //presenter
    private lateinit var presenter: ComputerListPresenter

    //JOB search cancleable
    val uiScope = CoroutineScope(Dispatchers.Main)
    private var textChangeCountJob: Job? = null
    private var mInputPencarian: String? = null

    //rcyclerview
    private lateinit var computersAdapter: ComputerListAdapter
    private var computersData: MutableList<ComputerListData.Result> = mutableListOf()

    //Dropdown dialog
    private lateinit var myDialog: Dialog
    private lateinit var branchDropdownOption: Array<String>
    private lateinit var locationDropdownOption: Array<String>
    private lateinit var divisionDropdownOption: Array<String>
    private lateinit var seatManajemenDropdownOption: Array<String>
    private lateinit var statusDropdownOption: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_computer_list)

        //Presenter inisiasi
        presenter = ComputerListPresenter(this)

        //INIT OPTION Array from string.xml
        branchDropdownOption = resources.getStringArray(R.array.branch_array)
        locationDropdownOption = resources.getStringArray(R.array.computer_location_array)
        divisionDropdownOption = resources.getStringArray(R.array.computer_division_array)
        seatManajemenDropdownOption =
            resources.getStringArray(R.array.computer_seat_manajemen_array)
        statusDropdownOption = resources.getStringArray(R.array.computer_status_array)

        resetStaticValue()


        //AMBIL DATA DARI ACTIVITY UNTUK INPUT TEXT mInputPencarian
        //JIKA PENCARIAN ADA, MATIKAN CHIP UTOMATIS
        val intentSearch = intent.getStringExtra(DATA_INTENT_DASHBOARD_COMPUTER_LIST)
        var isSearching = false
        if (!intentSearch.isNullOrEmpty()) {
            isSearching = true
            et_computerlist_searchbar.setQuery(intentSearch, false)
            presenter.getComputerDataSearch(intentSearch)
        }


        rv_computerlist.layoutManager = LinearLayoutManager(this)
        computersAdapter = ComputerListAdapter(this, computersData) {
            startActivity<DetailComputerActivity>(DATA_INTENT_COMPUTER_LIST_DETAIL to it)
        }
        rv_computerlist.adapter = computersAdapter


        //LISTENER CHIP, memanggil dialog filter
        chip_computerlist_filter.setOnClickListener {
            showFilterDialog()
        }


        //SEARCH TEXT CHANGE LISTENER
        et_computerlist_searchbar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                textChangeCountJob?.cancel()
                presenter.getComputerDataSearch(query!!)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                mInputPencarian = newText!!
                textChangeCountJob?.cancel()

                if (mInputPencarian!!.length > 2) {
                    textChangeCountJob = uiScope.launch {
                        delay(800L)
                        presenter.getComputerDataSearch(mInputPencarian!!)
                    }
                }
                return false
            }
        })


        // Get Computer List Pertama Kali
        if (!isSearching) {
            presenter.getComputerData(
                whatComputerBranch,
                whatComputerLocation,
                whatComputerDivision,
                whatComputerSeatManajemen,
                whatComputerStatus
            )
        }

        //tombol add computer
        bt_computerlist_tambah.setOnClickListener {
            //HIDE BUTTON IF MONITOR MODE
            if (App.prefs.userBranchSave == "ReadOnly" || App.prefs.isReadOnly || App.prefs.userBranchSave.isEmpty()) {
                longToast("Read Only User tidak dapat menambahkan komputer")
            } else {
                startActivity<AddComputerActivity>()
            }
        }

        //INIT dialog
        myDialog = Dialog(this)

        //HIDE KEYBOARD
        et_computerlist_searchbar.isFocusable = false
        et_computerlist_searchbar.clearFocus()

    }

    override fun hideLoading() {
        pb_computerlist.visibility = View.GONE
    }

    override fun showLoading() {
        pb_computerlist.visibility = View.VISIBLE
    }

    override fun showToast(notif: String) {
        toast(notif)
    }

    override fun showComputers(data: List<ComputerListData.Result>) {
        computersData.clear()
        computersData.addAll(data)
        runLayoutAnimation(rv_computerlist)
        computersAdapter.notifyDataSetChanged()
    }

    override fun updateUnit(unit: String) {
        tv_computerlist_unit.text = "Jumlah : $unit"
    }

    override fun onStop() {
        textChangeCountJob?.cancel()
        super.onStop()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

//    private fun recyclerAnimation(){
//        //animasi
//        val resId = R.anim.layout_animation_fall_down
//        val animation: LayoutAnimationController = AnimationUtils.loadLayoutAnimation(this, resId)
//        rv_computerlist.layoutAnimation = animation
//        //animasiEnd
//    }

    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        recyclerView.scheduleLayoutAnimation()
        recyclerView.invalidate()
    }

    private fun resetStaticValue() {
        whatComputerBranch = App.prefs.userBranchSave
        whatComputerLocation = ""
        whatComputerDivision = ""
        whatComputerSeatManajemen = ""
        whatComputerStatus = "Baik"
    }

    private fun showFilterDialog() {
        val branchIndexStart = branchDropdownOption.indexOf(whatComputerBranch)
        val locationIndexStart =
            if (whatComputerLocation.isEmpty()) 0 else locationDropdownOption.indexOf(
                whatComputerLocation
            )
        val divisionIndexStart =
            if (whatComputerDivision.isEmpty()) 0 else divisionDropdownOption.indexOf(
                whatComputerDivision
            )
        val seatIndexStart =
            if (whatComputerSeatManajemen.isEmpty()) 0 else seatManajemenDropdownOption.indexOf(
                whatComputerSeatManajemen
            )
        val statusIndexStart =
            if (whatComputerStatus.isEmpty()) 0 else statusDropdownOption.indexOf(whatComputerStatus)

        var branchSelected = ""
        var locationSelected = ""
        var divisionSelected = ""
        var seatSelected = ""
        var statusSelected = ""

        myDialog.setContentView(R.layout.dialog_filter_computer)

        //BRANCH DROPDOWN
        val branchDropdown: Spinner = myDialog.findViewById(R.id.sp_filter_computer_branch)
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

        //LOCATION DROPDOWN
        val locationDropdown: Spinner = myDialog.findViewById(R.id.sp_filter_computer_lokasi)
        locationDropdown.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, locationDropdownOption)

        locationDropdown.setSelection(locationIndexStart)
        locationDropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                locationSelected = locationDropdownOption[position]
            }

        }

        //DIVISION DROPDOWN
        val divisionDropdown: Spinner = myDialog.findViewById(R.id.sp_filter_computer_divisi)
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

        //SEAT DROPDOWN
        val seatDropdown: Spinner = myDialog.findViewById(R.id.sp_filter_computer_seat)
        seatDropdown.adapter =
            ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                seatManajemenDropdownOption
            )

        seatDropdown.setSelection(seatIndexStart)
        seatDropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                seatSelected = seatManajemenDropdownOption[position]
            }
        }

        //STATUS DROPDOWN
        val statusDropdown: Spinner = myDialog.findViewById(R.id.sp_filter_computer_status)
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
        val buttonComputerDialogFilter: Button =
            myDialog.findViewById(R.id.bt_filter_computer_apply)
        buttonComputerDialogFilter.setOnClickListener {
            whatComputerBranch = branchSelected
            whatComputerLocation =
                if (locationSelected == locationDropdownOption[0]) "" else locationSelected
            whatComputerDivision =
                if (divisionSelected == divisionDropdownOption[0]) "" else divisionSelected
            whatComputerSeatManajemen =
                if (seatSelected == seatManajemenDropdownOption[0]) "" else seatSelected
            whatComputerStatus =
                if (statusSelected == statusDropdownOption[0]) "" else statusSelected

            //CALL PRESENTER
            presenter.getComputerData(
                whatComputerBranch,
                whatComputerLocation,
                whatComputerDivision,
                whatComputerSeatManajemen,
                whatComputerStatus
            )
            myDialog.dismiss()
        }

        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog.show()
    }

    override fun onResume() {
        super.onResume()
        //JIKA KOMPUTER UPDATE TRUE < HANYA BISA TRUE JIKA ADA PENAMBAHAN KOMPUTER
        if (Statis.isComputerUpdate) {
            presenter.getComputerData(
                whatComputerBranch,
                whatComputerLocation,
                whatComputerDivision,
                whatComputerSeatManajemen,
                whatComputerStatus
            )
            Statis.isComputerUpdate = false
        }
    }
}
