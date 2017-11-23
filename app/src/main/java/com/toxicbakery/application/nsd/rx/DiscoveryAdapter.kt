package com.toxicbakery.application.nsd.rx

import android.net.nsd.NsdServiceInfo
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class DiscoveryAdapter : RecyclerView.Adapter<DiscoveryViewHolder>() {

    private val items: MutableList<DiscoveryRecord> = mutableListOf()

    override fun onBindViewHolder(holder: DiscoveryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscoveryViewHolder =
            LayoutInflater.from(parent.context)
                    .inflate(android.R.layout.simple_list_item_1, parent, false)
                    .let { DiscoveryViewHolder(it) }

    override fun getItemCount(): Int = items.size

    fun addItem(discoveryRecord: DiscoveryRecord) {
        val count = itemCount
        items.add(discoveryRecord)
        notifyItemInserted(count)
    }

    fun removeItem(discoveryRecord: DiscoveryRecord) {
        val count = itemCount
        items.remove(discoveryRecord)
        notifyItemRemoved(count)
    }

    fun clear() {
        val count = itemCount
        items.clear()
        notifyItemRangeRemoved(0, count)
    }

}

class DiscoveryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val nameTextView: TextView = itemView.findViewById(android.R.id.text1)

    fun bind(discoveryRecord: DiscoveryRecord) {
        nameTextView.text = discoveryRecord.name
    }

}

data class DiscoveryRecord(
        val name: String
)

fun NsdServiceInfo.toDiscoveryRecord() = DiscoveryRecord(serviceName)