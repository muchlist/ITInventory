package com.meretas.itinventory.dashboard

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.meretas.itinventory.PrivacyPolicyActivity
import com.meretas.itinventory.R
import com.meretas.itinventory.computer_inv.computer_list.ComputerListActivity
import com.meretas.itinventory.data.HistoryListData
import com.meretas.itinventory.computer_inv.history.HistoryDetailActivity
import com.meretas.itinventory.login.LoginActivity
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
    private lateinit var historyAdapter: HistoryAdapter
    private var historyData: MutableList<HistoryListData.Result> = mutableListOf()

    private lateinit var myDialog: Dialog
    private var doubleClickLogout = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        presenter = DashboardPresenter(this)

        //toolbar
        setSupportActionBar(toolbar_dashboard)

        //update info user
        if (App.prefs.userBranchSave.isNotEmpty()) {
            toolbar_dashboard.title = App.prefs.userNameSave
            toolbar_dashboard.subtitle = App.prefs.userBranchSave
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
        cv_dashboard_consumable.setOnClickListener {
            toast("Menu Consumable Item Belum Tersedia")
        }
        cv_dashboard_other.setOnClickListener {
            toast("Menu Other Item Belum Tersedia")
        }

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
            presenter.getCurrentUserInfo()
            presenter.getHistoryDashboard()
        }

        //dialog
        myDialog = Dialog(this)


        //Declare Animation
        val scaleToOne = AnimationUtils.loadAnimation(this, R.anim.scale_to_one)
        val scaleToTwo = AnimationUtils.loadAnimation(this, R.anim.scale_to_two)
        val scaleToThree = AnimationUtils.loadAnimation(this, R.anim.scale_to_three)
        //SetAnimation
        cv_dashboard_inventory.startAnimation(scaleToOne)
        cv_dashboard_consumable.startAnimation(scaleToTwo)
        cv_dashboard_other.startAnimation(scaleToThree)

        //HIDE KEYBOARD
        et_dashboard_searchbar.isFocusable = false
        et_dashboard_searchbar.clearFocus()

    }

    override fun getUserInfo(name: String, branch: String, isReadOnly: Boolean) {
        toolbar_dashboard.title = name
        toolbar_dashboard.subtitle = branch
        App.prefs.userNameSave = name
        App.prefs.userBranchSave = branch
        App.prefs.isReadOnly = isReadOnly
    }

    override fun showHistory(data: List<HistoryListData.Result>) {
        historyData.clear()
        historyData.addAll(data)
        historyAdapter.notifyDataSetChanged()

        //Declare Animation
        val topToBottom = AnimationUtils.loadAnimation(this, R.anim.fade_in_history)
        rv_history_dashboard.startAnimation(topToBottom)

        reloadButtonAppear()
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
            toast("Developed by Muchlis - IT Sampit")
        }
        logoPelindo.setOnClickListener{
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
            presenter.getCurrentUserInfo()
            myDialog.dismiss()
        }

        textAccountOne.setOnClickListener {
            App.prefs.userBranchSave = ""
            App.prefs.authTokenSave = App.prefs.authTokenOne
            presenter.getCurrentUserInfo()
            myDialog.dismiss()
        }

        accountTwo.setOnClickListener {
            App.prefs.userBranchSave = ""
            App.prefs.authTokenSave = App.prefs.authTokenTwo
            presenter.getCurrentUserInfo()
            myDialog.dismiss()
        }

        textAccountTwo.setOnClickListener {
            App.prefs.userBranchSave = ""
            App.prefs.authTokenSave = App.prefs.authTokenTwo
            presenter.getCurrentUserInfo()
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
            presenter.getHistoryDashboard()
            Statis.isHistoryUpdate = false
        }
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}
