package com.meretas.itinventory.dashboard

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.GridLayoutAnimationController
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.meretas.itinventory.PrivacyPolicyActivity
import com.meretas.itinventory.R
import com.meretas.itinventory.inv_computer.computer_list.ComputerListActivity
import com.meretas.itinventory.data.MenuData
import com.meretas.itinventory.data.HistoryListGeneralData
import com.meretas.itinventory.inv_cctv.cctv_list.CctvListActivity
import com.meretas.itinventory.inv_printer.printer_list.PrinterListActivity
import com.meretas.itinventory.inv_server.server_list.ServerListActivity
import com.meretas.itinventory.login.LoginActivity
import com.meretas.itinventory.inv_stock.stock_list.StockListActivity
import com.meretas.itinventory.services.Api
import com.meretas.itinventory.utils.App
import com.meretas.itinventory.utils.DATA_INTENT_DASHBOARD_COMPUTER_LIST
import com.meretas.itinventory.utils.DATA_INTENT_DASHBOARD_DETAIL_HISTORY
import com.meretas.itinventory.utils.Statis
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_dashboard.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class DashboardActivity : AppCompatActivity(), DashboarView {

    //presenter
    private lateinit var presenter: DashboardPresenter

    //recycler view
    private lateinit var historyGeneralAdapter: HistoryGeneralAdapter
    private var historyGeneralData: MutableList<HistoryListGeneralData.Result> = mutableListOf()

    private lateinit var myDialog: Dialog
    private var doubleClickLogout = false

    //gridview
    private lateinit var menuAdapter: MenuListAdapter
    private var menuDataData: MutableList<MenuData> = mutableListOf()

    private lateinit var historyArrayDropdown : Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        presenter = DashboardPresenter(this, Api.retrofitService)

        //toolbar
        setSupportActionBar(toolbar_dashboard)

        //update info user
        if (App.prefs.userBranchSave.isNotEmpty()) {
            toolbar_dashboard.title = App.prefs.userNameSave
            toolbar_dashboard.subtitle = App.prefs.userBranchSave
        }
        presenter.getCurrentUserInfo(App.prefs.authTokenSave)

        setRecyclerView()

        //HISTORY TEXT AND LOAD
        historyArrayDropdown = resources.getStringArray(R.array.history_select)
        tv_dashboard_history_selection.text = App.prefs.historySelected
        callGetHistoryPresenter()

        //SEARCHBAR LISTENER
        et_dashboard_searchbar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                startActivity<ComputerListActivity>(DATA_INTENT_DASHBOARD_COMPUTER_LIST to query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        bt_dashboard_reload.setOnClickListener {
            bt_dashboard_reload.visibility = View.INVISIBLE
            presenter.getCurrentUserInfo(App.prefs.authTokenSave)
            presenter.getComputerHistoryDashboard(App.prefs.authTokenSave)
        }

        //INIT dialog
        myDialog = Dialog(this)


        //MENU LIST ITEM GRIDVIEW
        menuDataData.apply {
            clear()
            add(MenuData(0, "Komputer", R.drawable.ic_029_computer))
            add(MenuData(1, "Stok", R.drawable.ic_049_stock))
            add(MenuData(2, "Printer", R.drawable.ic_041_printer))
            add(MenuData(3, "Server", R.drawable.ic_047_server))
            add(MenuData(4, "CCTV", R.drawable.ic_018_cctv))
        }

        val animation: Animation = AnimationUtils.loadAnimation(this, R.anim.grid_item_anim)
        val controller = GridLayoutAnimationController(animation, .1f, .3f)

        menuAdapter = MenuListAdapter(this, menuDataData) {

            when (it.id) {
                0 -> startActivity<ComputerListActivity>(DATA_INTENT_DASHBOARD_COMPUTER_LIST to "")
                1 -> startActivity<StockListActivity>()
                2 -> startActivity<PrinterListActivity>()
                3 -> startActivity<ServerListActivity>()
                4 -> startActivity<CctvListActivity>()
                else -> toast("Menu ${it.title} masih dalam tahap pengembangan")
            }
        }
        gv_dashboard_menu.layoutAnimation = controller
        gv_dashboard_menu.adapter = menuAdapter

        //SELECT HISTORY BUTTON
        tv_dashboard_history_selection.setOnClickListener {
            historySelector()
        }

        //HIDE KEYBOARD
        et_dashboard_searchbar.isFocusable = false
        et_dashboard_searchbar.clearFocus()

    }

    private fun setRecyclerView() {
        rv_history_dashboard.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        historyGeneralAdapter = HistoryGeneralAdapter(this, historyGeneralData) {
            startActivity<HistoryDetailActivity>(DATA_INTENT_DASHBOARD_DETAIL_HISTORY to it)
        }
        rv_history_dashboard.adapter = historyGeneralAdapter
        rv_history_dashboard.hasFixedSize()
    }

    private fun historySelector() {
        lateinit var dialog: AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih History")
        builder.setSingleChoiceItems(historyArrayDropdown, -1) { _, which ->
            val history = historyArrayDropdown[which]
            try {
                tv_dashboard_history_selection.text = history
                App.prefs.historySelected = history
                callGetHistoryPresenter()
            } catch (e: IllegalArgumentException) {
                toast("error")
            }
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }

    private fun callGetHistoryPresenter(){
        when(App.prefs.historySelected){
            //0 komputer 1 printer 2 server 3 cctv
            historyArrayDropdown[0] -> presenter.getComputerHistoryDashboard(App.prefs.authTokenSave)
            historyArrayDropdown[1] -> presenter.getPrinterHistoryDashboard(App.prefs.authTokenSave)
            historyArrayDropdown[3] -> presenter.getCctvHistoryDashboard(App.prefs.authTokenSave)
        }
    }

    override fun showHistory(generalData: List<HistoryListGeneralData.Result>) {
        historyGeneralData.clear()
        historyGeneralData.addAll(generalData)
        historyGeneralAdapter.notifyDataSetChanged()
        rv_history_dashboard.scrollToPosition(0)

        //Declare Animation
        val topToBottom = AnimationUtils.loadAnimation(this, R.anim.fade_in_history)
        rv_history_dashboard.startAnimation(topToBottom)

        reloadButtonAppear()
    }

    override fun getUserInfo(name: String, branch: String, isReadOnly: Boolean) {
        toolbar_dashboard.title = name
        toolbar_dashboard.subtitle = branch
        App.prefs.userNameSave = name
        App.prefs.userBranchSave = branch
        App.prefs.isReadOnly = isReadOnly
    }

    override fun hideProgressBarHistory() {
        pb_history_dashboard.visibility = View.GONE
    }

    override fun showProgressBarHistory() {
        pb_history_dashboard.visibility = View.VISIBLE
    }

    override fun showToastAndReload(notif: String) {
        toast(notif)
        //jika is history update true , akan me reload lagi di onresume
        Statis.isHistoryUpdate = true
        reloadButtonAppear()
    }

    private fun reloadButtonAppear() {
        val bottomToTop = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top)
        bt_dashboard_reload.visibility = View.VISIBLE
        bt_dashboard_reload.startAnimation(bottomToTop)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.overflow_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.profile -> {
                showAccountDialog()
            }
            R.id.about -> {
                showAboutDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAboutDialog() {
        myDialog.setContentView(R.layout.dialog_about)
        val logoMuchlis: ImageView = myDialog.findViewById(R.id.logo_muchlis)
        val logoPelindo: ImageView = myDialog.findViewById(R.id.logo_pelindo)
        val privacyPolicy: TextView = myDialog.findViewById(R.id.bt_privacy_policy)

        logoMuchlis.setOnClickListener {
            toast("Developed by Muchlis")
        }
        logoPelindo.setOnClickListener {
            toast("Supported by Pelindo Regional Kalimantan")
        }
        privacyPolicy.setOnClickListener {
            startActivity<PrivacyPolicyActivity>()
        }

        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog.show()
    }

    private fun showAccountDialog() {
        myDialog.setContentView(R.layout.dialog_account)
        val logoutButton: TextView = myDialog.findViewById(R.id.tv_logout)
        val accountOne: CircleImageView = myDialog.findViewById(R.id.iv_akun_satu)
        val accountTwo: CircleImageView = myDialog.findViewById(R.id.iv_akun_dua)
        val textAccountOne: TextView = myDialog.findViewById(R.id.tv_akun_satu)
        val textAccountTwo: TextView = myDialog.findViewById(R.id.tv_akun_dua)
        val addAccount: ImageView = myDialog.findViewById(R.id.iv_add_akun)

        //Jika auth token 2 kosong
        if (App.prefs.authTokenTwo.isEmpty()) {
            textAccountTwo.visibility = View.INVISIBLE
            accountTwo.visibility = View.INVISIBLE
        } else {
            addAccount.visibility = View.INVISIBLE
        }

        accountOne.setOnClickListener {
            App.prefs.userBranchSave = ""
            App.prefs.authTokenSave = App.prefs.authTokenOne
            presenter.getCurrentUserInfo(App.prefs.authTokenSave)
            myDialog.dismiss()
        }

        textAccountOne.setOnClickListener {
            App.prefs.userBranchSave = ""
            App.prefs.authTokenSave = App.prefs.authTokenOne
            presenter.getCurrentUserInfo(App.prefs.authTokenSave)
            myDialog.dismiss()
        }

        accountTwo.setOnClickListener {
            App.prefs.userBranchSave = ""
            App.prefs.authTokenSave = App.prefs.authTokenTwo
            presenter.getCurrentUserInfo(App.prefs.authTokenSave)
            myDialog.dismiss()
        }

        textAccountTwo.setOnClickListener {
            App.prefs.userBranchSave = ""
            App.prefs.authTokenSave = App.prefs.authTokenTwo
            presenter.getCurrentUserInfo(App.prefs.authTokenSave)
            myDialog.dismiss()
        }

        addAccount.setOnClickListener {
            myDialog.dismiss()
            startActivity<LoginActivity>()
            finish()

        }

        logoutButton.setOnClickListener {
            if (doubleClickLogout) {
                val pref = App.prefs
                pref.authTokenSave = ""
                pref.authTokenOne = ""
                pref.authTokenTwo = ""
                pref.userBranchSave = ""
                myDialog.dismiss()
                finish()
            } else {
                toast("Ketuk sekali lagi untuk logout")
            }
            doubleClickLogout = true
            Handler().postDelayed({ doubleClickLogout = false }, 2000)
        }

        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog.show()

    }

    override fun onResume() {
        super.onResume()
        if (Statis.isHistoryUpdate) {
            callGetHistoryPresenter()
            Statis.isHistoryUpdate = false
        }
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}
