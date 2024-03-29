package com.meretas.itinventory.inv_stock.stock_detail.stock_use_pager_fragment

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
import com.meretas.itinventory.inv_stock.stock_detail.DetailStockViewModel
import com.meretas.itinventory.inv_stock.stock_detail.stock_use_detail.StockUseDetailActivity
import com.meretas.itinventory.utils.*
import kotlinx.android.synthetic.main.fragment_consume_stock.*
import kotlinx.android.synthetic.main.fragment_consume_stock.view.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class ConsumeListStockFragment : Fragment() {

    private lateinit var viewModel: DetailStockViewModel
    private lateinit var consumeAdapter: ConsumeStockAdapter
    private var consumeData: MutableList<AddAndConsumeData.Result> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_consume_stock, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //INIT View Model
        viewModel = ViewModelProviders.of(activity!!).get(DetailStockViewModel::class.java)

        viewModel.isConsumeListLoading.observe(viewLifecycleOwner, Observer {
            refresh_stock_consumes.isRefreshing = it
        })

        viewModel.isConsumeListError.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                activity?.toast(it)
            }
        })

        viewModel.consumeData.observe(viewLifecycleOwner, Observer {
            this.consumeData.clear()
            this.consumeData.addAll(it)
            //runLayoutAnimation(rv_stock_additions)
            consumeAdapter.notifyDataSetChanged()
        })

        setRecyclerView(view)
        refresh_stock_consumes.setOnRefreshListener {
            viewModel.getConsumeList(viewModel.stockDetailData.value?.id ?: 0)
        }

        viewModel.getConsumeList(viewModel.stockDetailData.value?.id ?: 0)

    }

    private fun setRecyclerView(view: View) {
        view.rv_stock_consumes.layoutManager = LinearLayoutManager(activity)
        consumeAdapter = ConsumeStockAdapter(activity, consumeData) {
            activity?.startActivity<StockUseDetailActivity>(
                SOURCE_INTENT_STOCK_USE to FROM_CONSUME_STOCK,
                DATA_INTENT_STOCK_USE to it,
                SOURCE_INTENT_STOCK_ACTIVE to viewModel.stockDetailData.value?.active
            )
        }
        view.rv_stock_consumes.adapter = consumeAdapter
        view.rv_stock_consumes.setHasFixedSize(true)
    }

/*    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        recyclerView.scheduleLayoutAnimation()
        recyclerView.invalidate()
    }*/

    override fun onResume() {
        super.onResume()
        if (Statis.isStockChangeMinus) {
            viewModel.getConsumeList(viewModel.stockDetailData.value?.id ?: 0)
        }
    }

}