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
import com.imaec.notificationhelper.model.ContentRO
import java.text.SimpleDateFormat

class DetailAdapter(
    private val onClickContent: (ContentRO, Boolean) -> Unit
) : BaseAdapter() {

    private lateinit var packageName: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemDetailBinding.inflate(LayoutInflater.from(parent.context))
        return ItemViewHolder(binding as ItemDetailBinding)
    }

    override fun getItemCount(): Int = listItem.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.onBind(listItem[position] as ContentRO)
        }
    }

    inner class ItemViewHolder(val binding: ItemDetailBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SimpleDateFormat")
        fun onBind(item: ContentRO) {
            binding.apply {
                this.item = item
                this.packageName = this@DetailAdapter.packageName

                imageItemContent.setOnClickListener {
                    onClickContent(item, true)
                }
            }

            itemView.setOnClickListener {
                onClickContent(item, false)
            }
        }
    }

    fun setPackageName(packageName: String) {
        this.packageName = packageName
    }
}