package com.imaec.notificationhelper.ui.adapter

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.base.BaseAdapter
import com.imaec.notificationhelper.databinding.ItemAppBinding
import com.imaec.notificationhelper.model.AppData
import com.imaec.notificationhelper.model.IgnoreRO
import io.realm.RealmResults

class AppAdapter(
    val callback: (position: Int, isSelected: Boolean) -> Unit
) : BaseAdapter() {

    private val selectedItems = SparseBooleanArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemAppBinding.inflate(LayoutInflater.from(parent.context))
        return ItemViewHolder(binding as ItemAppBinding)
    }

    override fun getItemCount(): Int = listItem.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.onBind(listItem[position] as AppData, position)
        }
    }

    inner class ItemViewHolder(val binding: ItemAppBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: AppData, position: Int) {
            binding.apply {
                this.item = item

                itemView.setBackgroundColor(ContextCompat.getColor(binding.root.context, if (selectedItems.get(position, false)) android.R.color.white else R.color.colorPrimary))
                textItemName.setTextColor(ContextCompat.getColor(binding.root.context, if (selectedItems.get(position, false)) R.color.colorPrimary else android.R.color.white))
            }
//            glide
//                .load(item.icon)
//                .into(imageIcon)

//            textName.text = item.name


            itemView.setOnClickListener {
                if (selectedItems.get(position)) selectedItems.delete(position)
                else selectedItems.put(position, true)

                notifyItemChanged(position)

                callback(position, selectedItems.get(position))
            }
//            binding.root.setOnClickListener {
//                if (selectedItems.get(position)) selectedItems.delete(position)
//                else selectedItems.put(position, true)
//
//                notifyItemChanged(position)
//
//                callback(position, selectedItems.get(position))
//            }
        }
    }

    fun setSelectedItems(listSelectedItems: RealmResults<IgnoreRO>) {
        listItem.forEach { item ->
            if (item is AppData) {
                listSelectedItems.forEach {
                    if (item.packageName == it.packageName) {
                        selectedItems.put(listItem.indexOf(item), true)
                    }
                }
            }
        }
    }
}