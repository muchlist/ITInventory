package com.meretas.itinventory.inv_server.server_list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.meretas.itinventory.R
import com.meretas.itinventory.data.ServerListData
import kotlinx.android.synthetic.main.item_server_list.view.*

class ServerListAdapter(
    private val context: Context?,
    private val itemPrint: List<ServerListData.Result>,
    private val itemClick: (ServerListData.Result) -> Unit
) : RecyclerView.Adapter<ServerListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_server_list, parent, false)
        return ViewHolder(
            view,
            itemClick
        )
    }

    override fun getItemCount(): Int = itemPrint.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(itemPrint[position])
    }


    class ViewHolder(view: View, val itemClick: (ServerListData.Result) -> Unit) :
        RecyclerView.ViewHolder(view) {
        fun bindItem(items: ServerListData.Result) {

            itemView.tv_serverlist_name.text = items.serverName
            itemView.tv_serverlist_ip.text = items.ipAddress
            itemView.tv_serverlist_category.text = items.category
            itemView.tv_serverlist_condition.text = items.status[0].toString() //huruf pertama
            itemView.tv_serverlist_merk.text = items.merkModel

            //onClick
            itemView.setOnClickListener { itemClick(items) }
        }


    }
}