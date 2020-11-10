package com.imaec.notificationhelper.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imaec.notificationhelper.base.BaseAdapter
import com.imaec.notificationhelper.databinding.ItemNotificationBinding
import com.imaec.notificationhelper.model.NotificationRO

class NotificationAdapter : BaseAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context))
        return ItemViewHolder(binding as ItemNotificationBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.onBind(listItem[position] as NotificationRO)
        }
    }

    inner class ItemViewHolder(val binding: ItemNotificationBinding): RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun onBind(item: NotificationRO) {
            binding.apply {
                this.item = item
                this.size = item.contents.size
            }

            itemView.setOnClickListener {
                onClick(item)
            }
        }
    }
}