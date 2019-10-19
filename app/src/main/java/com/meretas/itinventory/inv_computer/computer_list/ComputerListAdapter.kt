package com.meretas.itinventory.inv_computer.computer_list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.meretas.itinventory.R
import com.meretas.itinventory.data.ComputerListData
import kotlinx.android.synthetic.main.item_computer_list.view.*

class ComputerListAdapter(
    private val context: Context?,
    private val itemComputer: List<ComputerListData.Result>,
    private val itemClick: (ComputerListData.Result) -> Unit
) : RecyclerView.Adapter<ComputerListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_computer_list, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int = itemComputer.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(itemComputer[position])
    }


    class ViewHolder(view: View, val itemClick: (ComputerListData.Result) -> Unit) :
        RecyclerView.ViewHolder(view) {
        fun bindItem(items: ComputerListData.Result) {

            itemView.tv_computerlist_name.text = items.clientName
            itemView.tv_computerlist_ip.text = items.ipAddress
            itemView.tv_computerlist_division.text = items.division
            itemView.tv_computerlist_islowspec.text = if (items.lowSpec) "L" else "-"
            itemView.tv_computerlist_seat.text = if (items.seatManagement) "M" else "-"
            itemView.tv_computerlist_condition.text = items.status[0].toString() //Huruf Pertama
            itemView.tv_computerlist_branch.text = items.branch

            //onClick
            itemView.setOnClickListener { itemClick(items) }
        }


    }
}