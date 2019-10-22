package com.meretas.itinventory.dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.meretas.itinventory.R
import com.meretas.itinventory.data.HistoryListGeneralData
import kotlinx.android.synthetic.main.item_history_dashboard.view.*

class HistoryGeneralAdapter(
    private val context: Context?,
    private val itemHistoryGeneral: List<HistoryListGeneralData.Result>,
    private val itemClick: (HistoryListGeneralData.Result) -> Unit
) : RecyclerView.Adapter<HistoryGeneralAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_history_dashboard, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int = itemHistoryGeneral.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(itemHistoryGeneral[position])
    }


    class ViewHolder(view: View, val itemClick: (HistoryListGeneralData.Result) -> Unit) :
        RecyclerView.ViewHolder(view) {
        fun bindItem(items: HistoryListGeneralData.Result) {

            itemView.tv_history_name.text = items.computer
            itemView.tv_history_status.text = items.statusHistory
            itemView.tv_history_note.text = items.note
            itemView.tv_history_author.text = items.author
            itemView.tv_history_date.text = items.createdAt
            itemView.tv_history_branch.text = items.branch

            //onClick
            itemView.setOnClickListener { itemClick(items) }
        }


    }
}