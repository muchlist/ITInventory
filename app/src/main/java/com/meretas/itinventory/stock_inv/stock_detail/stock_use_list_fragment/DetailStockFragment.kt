package com.meretas.itinventory.stock_inv.stock_detail.stock_use_list_fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.meretas.itinventory.R
import com.meretas.itinventory.stock_inv.stock_detail.DetailStockViewModel
import com.meretas.itinventory.utils.Statis
import kotlinx.android.synthetic.main.fragment_detail_stock.*
import org.jetbrains.anko.toast


class DetailStockFragment : Fragment() {

    private lateinit var viewModel: DetailStockViewModel

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

                val added: Int = stockAdded ?: 0
                val used: Int = stockUsed ?: 0

                tv_detail_stock_additions.text = added.toString()
                tv_detail_stock_consume.text = used.toString()
                tv_detail_stock_resultstock.text = (added - used).toString()
            }
        })

        viewModel.isstockDetailError.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                activity?.toast(it)
            }
        })


    }

    override fun onResume() {
        super.onResume()
        if(Statis.isStockChangePlus || Statis.isStockChangeMinus){
            viewModel.getStockRefresh(viewModel.stockDetailData.value?.id?:0)
        }
    }
}
