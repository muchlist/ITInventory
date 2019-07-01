package com.meretas.itinventory.dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.meretas.itinventory.R
import com.meretas.itinventory.data.HistoryListData
import kotlinx.android.synthetic.main.activity_history_detail.view.*
import kotlinx.android.synthetic.main.item_history_dashboard.view.*

class HistoryAdapter(
    private val context: Context?,
    private val itemHistory: List<HistoryListData.Result>,
    private val itemClick: (HistoryListData.Result) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_history_dashboard, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int = itemHistory.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(itemHistory[position])
    }


    class ViewHolder(view: View, val itemClick: (HistoryListData.Result) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bindItem(items: HistoryListData.Result) {

            itemView.tv_history_pc.text = "PC " + items.computer
            itemView.tv_history_status.text = items.statusHistory
            itemView.tv_history_note.text = items.note
            itemView.tv_history_name.text = items.author
            itemView.tv_history_date.text = items.createdAt

            //onClick
            itemView.setOnClickListener { itemClick(items) }
        }


    }
}