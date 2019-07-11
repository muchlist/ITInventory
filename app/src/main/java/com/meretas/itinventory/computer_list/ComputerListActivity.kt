package com.meretas.itinventory.computer_list

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.meretas.itinventory.R
import com.meretas.itinventory.add_computer.AddComputerActivity
import com.meretas.itinventory.computer_detail.DetailComputerActivity
import com.meretas.itinventory.data.ComputerListData
import com.meretas.itinventory.utils.*
import kotlinx.android.synthetic.main.activity_computer_list.*
import kotlinx.coroutines.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class ComputerListActivity : AppCompatActivity(), ComputerListView {

    //presenter
    private lateinit var presenter: ComputerListPresenter

    //nilai branchText
    private var branchText: String = ""
    private var locationText: String = ""

    //JOB search cancleable
    val uiScope = CoroutineScope(Dispatchers.Main)
    private var textChangeCountJob: Job? = null
    private var mInputPencarian: String? = null

    //rcyclerview
    private lateinit var computersAdapter: ComputerListAdapter
    private var computersData: MutableList<ComputerListData.Result> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_computer_list)

        //Presenter inisiasi
        presenter = ComputerListPresenter(this)


        //AMBIL DATA DARI ACTIVITY UNTUK INPUT TEXT mInputPencarian
        //JIKA PENCARIAN ADA, MATIKAN CHIP UTOMATIS
        val intent = intent.getStringExtra(DATA_INTENT_DASHBOARD_COMPUTER_LIST)
        var isSearching = false
        if (!intent.isNullOrEmpty()) {
            isSearching = true
            et_computerlist_searchbar.setQuery(intent, false)
            presenter.getComputerDataSearch(intent)
        }


        rv_computerlist.layoutManager = LinearLayoutManager(this)
        computersAdapter = ComputerListAdapter(this, computersData) {
            startActivity<DetailComputerActivity>(DATA_INTENT_COMPUTER_LIST_DETAIL to it)
        }
        rv_computerlist.adapter = computersAdapter


        //LISTENER CHIP BRANCH, memanggil data melalui klik chip ini
        chip_banjarmasin.setOnClickListener {
            showLocationChoices()
            branchText = BANJARMASIN
            locationText = ""
            presenter.getComputerData(branchText, locationText)
        }
        chip_sampit.setOnClickListener {
            hideLocationChoices()
            branchText = SAMPIT
            locationText = ""
            presenter.getComputerData(branchText, locationText)
        }
        chip_bagendang.setOnClickListener {
            hideLocationChoices()
            branchText = BAGENDANG
            locationText = ""
            presenter.getComputerData(branchText, locationText)
        }
        chip_kotabaru.setOnClickListener {
            hideLocationChoices()
            branchText = KOTABARU
            locationText = ""
            presenter.getComputerData(branchText, locationText)
        }
        chip_batulicin.setOnClickListener {
            hideLocationChoices()
            branchText = BATULICIN
            locationText = ""
            presenter.getComputerData(branchText, locationText)
        }
        chip_mekarputih.setOnClickListener {
            hideLocationChoices()
            branchText = MEKARPUTIH
            locationText = ""
            presenter.getComputerData(branchText, locationText)
        }
        chip_kumai.setOnClickListener {
            hideLocationChoices()
            branchText = KUMAI
            locationText = ""
            presenter.getComputerData(branchText, locationText)
        }
        chip_bumiharjo.setOnClickListener {
            hideLocationChoices()
            branchText = BUMIHARJO
            locationText = ""
            presenter.getComputerData(branchText, locationText)
        }

        //LISTENER CHIP LOCATION
        chip_regional.setOnClickListener {
            locationText = REGIONAL
            presenter.getComputerData(branchText, locationText)
        }
        chip_trisakti.setOnClickListener {
            locationText = TRISAKTI
            presenter.getComputerData(branchText, locationText)
        }
        chip_pulpis.setOnClickListener {
            locationText = PULPIS
            presenter.getComputerData(branchText, locationText)
        }
        chip_tpkb.setOnClickListener {
            locationText = TPKB
            presenter.getComputerData(branchText, locationText)
        }
        chip_none.setOnClickListener {
            locationText = ""
            presenter.getComputerData(branchText, locationText)
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


        // Pilih Chip Saat Pertama kali masuk aktifity
        if (!isSearching) {
            //toast("is not seaarching")
            when (App.prefs.userBranchSave) {
                "Sampit" -> chip_sampit.performClick()
                "Bagendang" -> chip_bagendang.performClick()
                "Kotabaru" -> chip_kotabaru.performClick()
                "Batulicin" -> chip_batulicin.performClick()
                "Mekarputih" -> chip_mekarputih.performClick()
                "Kumai" -> chip_kumai.performClick()
                "Bumiharjo" -> chip_bumiharjo.performClick()
                else -> chip_banjarmasin.performClick()
            }
        }

        //tombol add computer
        bt_computerlist_tambah.setOnClickListener {
            //HIDE BUTTON IF MONITOR MODE
            if (App.prefs.userBranchSave == "ReadOnly" || App.prefs.isReadOnly) {
                longToast("Read Only User tidak dapat menambahkan komputer")
            } else {
                startActivity<AddComputerActivity>()
            }
        }

        //HIDE KEYBOARD
        et_computerlist_searchbar.setFocusable(false)
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

    override fun showUpdateComputers() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLocationChoices() {
        horizontalScrollView2.visibility = View.GONE
    }

    override fun showLocationChoices() {
        horizontalScrollView2.visibility = View.VISIBLE
    }

    override fun updateUnit(unit: String) {
        tv_computerlist_unit.text = "Jumlah : " + unit
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

    override fun onResume() {
        super.onResume()
        //JIKA KOMPUTER UPDATE TRUE < HANYA BISA TRUE JIKA ADA PENAMBAHAN KOMPUTER
        if (Statis.isComputerUpdate) {
            when (App.prefs.userBranchSave) {
                "Sampit" -> chip_sampit.performClick()
                "Bagendang" -> chip_bagendang.performClick()
                "Kotabaru" -> chip_kotabaru.performClick()
                "Batulicin" -> chip_batulicin.performClick()
                "Mekarputih" -> chip_mekarputih.performClick()
                "Kumai" -> chip_kumai.performClick()
                "Bumiharjo" -> chip_bumiharjo.performClick()
                else -> chip_banjarmasin.performClick()
            }
            Statis.isComputerUpdate = false
        }
    }
}
