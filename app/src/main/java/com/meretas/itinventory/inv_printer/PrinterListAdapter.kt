package com.meretas.itinventory.inv_printer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.meretas.itinventory.R
import com.meretas.itinventory.data.PrinterListData
import kotlinx.android.synthetic.main.item_printer_list.view.*

class PrinterListAdapter(
    private val context: Context?,
    private val itemPrint: List<PrinterListData.Result>,
    private val itemClick: (PrinterListData.Result) -> Unit
) : RecyclerView.Adapter<PrinterListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_printer_list, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int = itemPrint.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(itemPrint[position])
    }


    class ViewHolder(view: View, val itemClick: (PrinterListData.Result) -> Unit) :
        RecyclerView.ViewHolder(view) {
        fun bindItem(items: PrinterListData.Result) {

            itemView.tv_printerlist_name.text = items.printerName
            itemView.tv_printerlist_ip.text = items.ipAddress
            itemView.tv_printerlist_division.text = "${items.division} ${items.branch}"
            itemView.tv_printerlist_condition.text = items.status[0].toString() //huruf pertama
            itemView.tv_printerlist_merk.text = items.merkModel

            //onClick
            itemView.setOnClickListener { itemClick(items) }
        }


    }
}