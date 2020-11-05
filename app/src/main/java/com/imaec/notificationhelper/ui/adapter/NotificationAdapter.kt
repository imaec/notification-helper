package com.imaec.notificationhelper.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.utils.Utils
import com.imaec.notificationhelper.base.BaseAdapter
import com.imaec.notificationhelper.databinding.ItemNotificationBinding
import com.imaec.notificationhelper.model.NotificationRO

class NotificationAdapter(val glide: RequestManager) : BaseAdapter() {

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

        private val imageIcon by lazy { binding.root.findViewById<ImageView>(R.id.image_item_icon) }
        private val textName by lazy { binding.root.findViewById<TextView>(R.id.text_item_name) }

        @SuppressLint("SetTextI18n")
        fun onBind(item: NotificationRO) {
            glide
                .load(Utils.getAppIcon(binding.root.context, item.packageName) ?: ContextCompat.getDrawable(binding.root.context, R.mipmap.ic_launcher))
                .into(imageIcon)
            textName.text = "${item.appName} (${item.contents.size})"

            itemView.setOnClickListener {
                onClick(item)
            }
        }
    }
}