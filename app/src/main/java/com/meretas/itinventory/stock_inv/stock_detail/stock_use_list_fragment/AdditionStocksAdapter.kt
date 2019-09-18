package com.meretas.itinventory.stock_inv.stock_detail.stock_use_list_fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.meretas.itinventory.R
import com.meretas.itinventory.data.AddAndConsumeData
import kotlinx.android.synthetic.main.item_stock_addition.view.*

class AdditionStocksAdapter (
                             private val context: Context?,
                             private val itemHistory: List<AddAndConsumeData.Result>,
                             private val itemClick: (AddAndConsumeData.Result) -> Unit
) : RecyclerView.Adapter<AdditionStocksAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_stock_addition, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int = itemHistory.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(itemHistory[position])
    }

    class ViewHolder(view: View, val itemClick: (AddAndConsumeData.Result) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bindItem(items: AddAndConsumeData.Result) {

            with(items){
                itemView.tv_stock_add_nomer.text = eventNumber
                itemView.tv_stock_add_note.text = note
                itemView.tv_stock_add_value.text = qty.toString()
                itemView.tv_stock_add_unit.text = unit
                itemView.tv_stock_add_user.text = author
                itemView.tv_stock_add_date.text = createdAt
            }

            //onClick
            itemView.setOnClickListener { itemClick(items) }
        }

    }
}