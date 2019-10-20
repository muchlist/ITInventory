package com.meretas.itinventory.dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.meretas.itinventory.R
import com.meretas.itinventory.data.HistoryListComputerData
import kotlinx.android.synthetic.main.item_history_dashboard.view.*

class HistoryComputerAdapter(
    private val context: Context?,
    private val itemHistoryComputer: List<HistoryListComputerData.Result>,
    private val itemClick: (HistoryListComputerData.Result) -> Unit
) : RecyclerView.Adapter<HistoryComputerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_history_dashboard, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int = itemHistoryComputer.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(itemHistoryComputer[position])
    }


    class ViewHolder(view: View, val itemClick: (HistoryListComputerData.Result) -> Unit) :
        RecyclerView.ViewHolder(view) {
        fun bindItem(items: HistoryListComputerData.Result) {

            itemView.tv_history_name.text = "PC " + items.computer
            itemView.tv_history_status.text = items.statusHistory
            itemView.tv_history_note.text = items.note
            itemView.tv_history_author.text = items.author
            itemView.tv_history_date.text = items.createdAt

            //onClick
            itemView.setOnClickListener { itemClick(items) }
        }


    }
}