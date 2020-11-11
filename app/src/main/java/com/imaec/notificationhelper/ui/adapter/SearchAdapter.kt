package com.imaec.notificationhelper.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imaec.notificationhelper.TYPE_CONTENT
import com.imaec.notificationhelper.TYPE_NAME
import com.imaec.notificationhelper.base.BaseAdapter
import com.imaec.notificationhelper.databinding.ItemDetailBinding
import com.imaec.notificationhelper.databinding.ItemNotificationBinding
import com.imaec.notificationhelper.model.ContentRO
import com.imaec.notificationhelper.model.NotificationRO

class SearchAdapter(
    private val onClickNotification: (NotificationRO) -> Unit,
    private val onClickContent: (ContentRO, Boolean) -> Unit
) : BaseAdapter() {

    override fun getItemViewType(position: Int): Int {
        return when (listItem[position]) {
            is NotificationRO -> TYPE_NAME
            is ContentRO -> TYPE_CONTENT
            else -> TYPE_NAME
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context))
        var vh: RecyclerView.ViewHolder = NameViewHolder(binding as ItemNotificationBinding)
        when (viewType) {
            TYPE_NAME -> {
                binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context))
                vh = NameViewHolder(binding as ItemNotificationBinding)
            }
            TYPE_CONTENT -> {
                binding = ItemDetailBinding.inflate(LayoutInflater.from(parent.context))
                vh = ContentViewHolder(binding as ItemDetailBinding)
            }
        }
        return vh
    }

    override fun getItemCount(): Int = listItem.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NameViewHolder -> {
                holder.onBind(listItem[position] as NotificationRO)
            }
            is ContentViewHolder -> {
                holder.onBind(listItem[position] as ContentRO)
            }
        }
    }

    inner class NameViewHolder(val binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun onBind(item: NotificationRO) {
            binding.apply {
                this.item = item
                this.size = item.contents.size
            }

            itemView.setOnClickListener {
                onClickNotification(item)
            }
        }
    }

    inner class ContentViewHolder(val binding: ItemDetailBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SimpleDateFormat")
        fun onBind(item: ContentRO) {
            binding.apply {
                this.item = item

                imageItemContent.setOnClickListener {
                    onClickContent(item, true)
                }
            }

            itemView.setOnClickListener {
                onClickContent(item, false)
            }
        }
    }
}