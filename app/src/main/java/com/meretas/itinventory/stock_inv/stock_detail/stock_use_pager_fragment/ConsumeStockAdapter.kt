package com.meretas.itinventory.stock_inv.stock_detail.stock_use_pager_fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.meretas.itinventory.R
import com.meretas.itinventory.data.AddAndConsumeData
import kotlinx.android.synthetic.main.item_stock_consume.view.*

class ConsumeStockAdapter (
    private val context: Context?,
    private val itemConsume: List<AddAndConsumeData.Result>,
    private val itemClick: (AddAndConsumeData.Result) -> Unit
) : RecyclerView.Adapter<ConsumeStockAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_stock_consume, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int = itemConsume.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(itemConsume[position])
    }

    class ViewHolder(view: View, val itemClick: (AddAndConsumeData.Result) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bindItem(items: AddAndConsumeData.Result) {

            with(items){
                itemView.tv_stock_con_nomer.text = eventNumber
                itemView.tv_stock_con_note.text = note
                itemView.tv_stock_con_value.text = qty.toString()
                itemView.tv_stock_con_unit.text = unit
                itemView.tv_stock_con_user.text = author
                itemView.tv_stock_con_date.text = createdAt
            }

            //onClick
            itemView.setOnClickListener { itemClick(items) }
        }

    }
}