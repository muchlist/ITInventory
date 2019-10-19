package com.meretas.itinventory.inv_cctv.cctv_list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.meretas.itinventory.R
import com.meretas.itinventory.data.CctvListData
import kotlinx.android.synthetic.main.item_cctv_list.view.*

class CctvListAdapter(
    private val context: Context?,
    private val itemStock: List<CctvListData.Result>,
    private val itemClick: (CctvListData.Result) -> Unit
) : RecyclerView.Adapter<CctvListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_cctv_list, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int = itemStock.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(itemStock[position])
    }


    class ViewHolder(view: View, val itemClick: (CctvListData.Result) -> Unit) :
        RecyclerView.ViewHolder(view) {
        fun bindItem(items: CctvListData.Result) {

            itemView.tv_cctvlist_name.text = items.cctvName
            itemView.tv_cctvlist_ip.text = items.ipAddress
            itemView.tv_cctvlist_location.text = items.location
            itemView.tv_cctvlist_condition.text = items.status[0].toString() //huruf pertama
            itemView.tv_cctvlist_branch.text = items.branch

            //onClick
            itemView.setOnClickListener { itemClick(items) }
        }


    }
}