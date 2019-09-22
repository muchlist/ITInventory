package com.meretas.itinventory.stock_inv.stock_detail.stock_use_pager_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.meretas.itinventory.R
import com.meretas.itinventory.data.AddAndConsumeData
import com.meretas.itinventory.stock_inv.stock_detail.DetailStockViewModel
import com.meretas.itinventory.stock_inv.stock_detail.stock_use_detail.StockUseDetailActivity
import com.meretas.itinventory.utils.DATA_INTENT_STOCK_USE
import com.meretas.itinventory.utils.FROM_ADDITION_STOCK
import com.meretas.itinventory.utils.SOURCE_INTENT_STOCK_ACTIVE
import com.meretas.itinventory.utils.SOURCE_INTENT_STOCK_USE
import com.meretas.itinventory.utils.Statis.Companion.isStockChangePlus
import kotlinx.android.synthetic.main.fragment_addition_stock.*
import kotlinx.android.synthetic.main.fragment_addition_stock.view.*
import org.jetbrains.anko.toast
import org.jetbrains.anko.startActivity


class AdditionListStockFragment : Fragment() {

    private lateinit var viewModel: DetailStockViewModel
    private lateinit var additionAdapter: AdditionStocksAdapter
    private var additionsData: MutableList<AddAndConsumeData.Result> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_addition_stock, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //INIT View Model
        viewModel = ViewModelProviders.of(activity!!).get(DetailStockViewModel::class.java)

        viewModel.isAdditionListLoading.observe(viewLifecycleOwner, Observer {
            refresh_stock_additions.isRefreshing = it
        })

        viewModel.isAdditionListError.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                activity?.toast(it)
            }
        })

        viewModel.additionData.observe(viewLifecycleOwner, Observer {
            this.additionsData.clear()
            this.additionsData.addAll(it)
            //runLayoutAnimation(rv_stock_additions)
            additionAdapter.notifyDataSetChanged()
        })

        setRecyclerView(view)

        //PULL TO REFRESH
        refresh_stock_additions.setOnRefreshListener {
            viewModel.getAdditionList(viewModel.stockDetailData.value?.id?:0)
        }

        viewModel.getAdditionList(viewModel.stockDetailData.value?.id?:0)

    }

    private fun setRecyclerView(view: View) {
        view.rv_stock_additions.layoutManager = LinearLayoutManager(activity)
        additionAdapter = AdditionStocksAdapter(activity, additionsData) {
            activity?.startActivity<StockUseDetailActivity>(
                SOURCE_INTENT_STOCK_USE to FROM_ADDITION_STOCK,
                DATA_INTENT_STOCK_USE to it,
                SOURCE_INTENT_STOCK_ACTIVE to viewModel.stockDetailData.value?.active
            )
        }
        view.rv_stock_additions.adapter = additionAdapter
        view.rv_stock_additions.setHasFixedSize(true)
    }

/*    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        recyclerView.scheduleLayoutAnimation()
        recyclerView.invalidate()
    }*/

    override fun onResume() {
        super.onResume()
        if(isStockChangePlus){
            viewModel.getAdditionList(viewModel.stockDetailData.value?.id?:0)
        }
    }

}