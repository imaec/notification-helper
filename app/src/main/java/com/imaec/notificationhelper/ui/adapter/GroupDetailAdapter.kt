package com.imaec.notificationhelper.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.utils.Utils
import com.imaec.notificationhelper.ui.view.activity.ImageActivity
import com.imaec.notificationhelper.base.BaseAdapter
import com.imaec.notificationhelper.databinding.ItemDetailBinding
import com.imaec.notificationhelper.databinding.ItemGroupDetailBinding
import com.imaec.notificationhelper.model.ContentRO
import com.imaec.notificationhelper.model.GroupDetailData
import java.text.SimpleDateFormat

class GroupDetailAdapter : BaseAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemGroupDetailBinding.inflate(LayoutInflater.from(parent.context))
        return ItemViewHolder(binding as ItemGroupDetailBinding)
    }

    override fun getItemCount(): Int = listItem.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.onBind(listItem[position] as GroupDetailData)
        }
    }

    inner class ItemViewHolder(val binding: ItemGroupDetailBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SimpleDateFormat")
        fun onBind(item: GroupDetailData) {
            binding.apply {
                this.item = item
                this.sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            }

            itemView.setOnClickListener {
                onClick(item)
            }
        }
    }
}