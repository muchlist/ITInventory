package com.meretas.itinventory.stock_inv.stock_list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.meretas.itinventory.R
import com.meretas.itinventory.data.StockListData
import kotlinx.android.synthetic.main.item_stock_list.view.*

class StocklistAdapter(
    private val context: Context?,
    private val itemStock: List<StockListData.Result>,
    private val itemClick: (StockListData.Result) -> Unit
) : RecyclerView.Adapter<StocklistAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_stock_list, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int = itemStock.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(itemStock[position])
    }


    class ViewHolder(view: View, val itemClick: (StockListData.Result) -> Unit) :
        RecyclerView.ViewHolder(view) {
        fun bindItem(items: StockListData.Result) {

//            var lowspec = "  "
//            if(items.seatManagement){
//                lowspec = "-M"
//            } else if (items.lowSpec){
//                lowspec = "-L"
//            }

            val stockAdded = items.stockAdded ?: 0
            val stockUsed = items.stockUsed ?: 0
            val stockCurrent = stockAdded - stockUsed

            itemView.tv_stock_name.text = items.stockName
            itemView.tv_stock_category.text = items.category
            itemView.tv_stock_branch.text = items.branch
            itemView.tv_stock_note.text = items.note
            itemView.tv_stock_id.text = "# ${items.id}"
            itemView.tv_stock_sisa_value.text = stockCurrent.toString()
            itemView.tv_stock_unit.text = items.unit

            if (stockCurrent >= items.threshold && items.active) {
                itemView.iv_stock_indicator.visibility = View.INVISIBLE
            } else {
                itemView.iv_stock_indicator.visibility = View.VISIBLE
            }

            //onClick
            itemView.setOnClickListener { itemClick(items) }
        }


    }
}