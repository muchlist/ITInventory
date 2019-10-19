package com.meretas.itinventory.inv_stock.stock_detail.stock_use_pager_fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.meretas.itinventory.R
import com.meretas.itinventory.inv_stock.stock_detail.DetailStockViewModel
import com.meretas.itinventory.inv_stock.stock_edit.EditStockActivity
import com.meretas.itinventory.utils.*
import com.meretas.itinventory.utils.Statis.Companion.isStockUpdate
import kotlinx.android.synthetic.main.fragment_detail_stock.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class DetailStockFragment : Fragment() {

    private lateinit var viewModel: DetailStockViewModel
    private var statusActive: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_stock, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(activity!!).get(DetailStockViewModel::class.java)

        viewModel.stockDetailData.observe(viewLifecycleOwner, Observer {
            with(it) {
                tv_detail_stock_name.text = stockName
                tv_detail_stock_branch.text = branch
                tv_detail_stock_category.text = category
                tv_detail_stock_status.text = if (active) "Aktif" else "Nonaktif"
                tv_detail_stock_thresold.text = threshold.toString()
                tv_detail_stock_create_date.text = createdAt
                tv_detail_stock_unit.text = unit
                tv_detail_stock_note.text = note
                bt_detail_stock_nonaktif.text = if (active) "Nonaktifkan" else "aktifkan"
                statusActive = active

                val added: Int = stockAdded ?: 0
                val used: Int = stockUsed ?: 0

                tv_detail_stock_additions.text = added.toString()
                tv_detail_stock_consume.text = used.toString()
                tv_detail_stock_resultstock.text = (added - used).toString()

                //BUTTON
                buttonDisable(branch, active)
            }
        })

        viewModel.isStockDetailError.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                activity?.toast(it)
            }
        })

        viewModel.isStatusChangeError.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                activity?.toast(it)
            }
        })


    }

    override fun onResume() {
        super.onResume()
        if (Statis.isStockChangePlus || Statis.isStockChangeMinus || isStockUpdate) {
            viewModel.getStockRefresh(viewModel.stockDetailData.value?.id ?: 0)
        }
    }

    private fun changeStatus() {
        when {
            statusActive -> viewModel.changeStatus(
                viewModel.stockDetailData.value?.id ?: 0,
                STOCK_NON_ACTIVE
            )
            else -> viewModel.changeStatus(viewModel.stockDetailData.value?.id ?: 0, STOCK_ACTIVE)
        }
        isStockUpdate = true
    }

    private fun buttonDisable(branch: String, active: Boolean) {
        if (App.prefs.userBranchSave != branch || App.prefs.isReadOnly) {
            bt_detail_stock_edit.disable()
            bt_detail_stock_nonaktif.disable()
        } else {
            //BUTTON STATUS CHANGE
            bt_detail_stock_nonaktif.setOnClickListener {
                activity?.toast("Tahan tombol selama 2 detik untuk merubah status")
            }
            bt_detail_stock_nonaktif.setOnLongClickListener {
                changeStatus()
                return@setOnLongClickListener true
            }
            if (active) {
                //BUTTON EDIT
                bt_detail_stock_edit.enable()
                bt_detail_stock_edit.setOnClickListener {
                    activity?.startActivity<EditStockActivity>(
                        DATA_INTENT_STOCK_DETAIL to viewModel.stockDetailData.value
                    )
                }
            } else {
                bt_detail_stock_edit.disable()
            }
        }
    }

}
