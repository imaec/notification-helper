package com.imaec.notificationhelper.ui.adapter

import android.util.Log
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
import com.imaec.notificationhelper.ui.view.fragment.SettingFragment
import io.realm.RealmResults

class AppAdapter(val glide: RequestManager, val callback: SettingFragment.IgnoreCallback) : BaseAdapter() {

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

        private val imageIcon by lazy { binding.root.findViewById<ImageView>(R.id.image_item_icon) }
        private val textName by lazy { binding.root.findViewById<TextView>(R.id.text_item_name) }

        fun onBind(item: AppData, position: Int) {
            glide
                .load(item.icon)
                .into(imageIcon)

            textName.text = item.name
            Log.d(TAG, "    ## name : ${item.name}")

            if (selectedItems.get(position, false)) {
                itemView.setBackgroundColor(ContextCompat.getColor(binding.root.context, android.R.color.white))
                textName.setTextColor(ContextCompat.getColor(binding.root.context, R.color.colorPrimary))
            } else {
                itemView.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.colorPrimary))
                textName.setTextColor(ContextCompat.getColor(binding.root.context, android.R.color.white))
            }

            binding.root.setOnClickListener {
                if (selectedItems.get(position)) selectedItems.delete(position)
                else selectedItems.put(position, true)

                notifyItemChanged(position)

                callback.onIgnore(position, selectedItems.get(position))
            }
        }
    }

    fun addItem(item: AppData) {
        listItem.add(item)
    }

    fun sort() {
        val listTemp = listItem.sortedBy { if (it is AppData) it.name else null }
//        val listTemp = listItem.sortedWith(compareBy { it.name })
        listItem.clear()
        listTemp.forEach {
            listItem.add(it)
        }
    }

    fun setSelectedItems(listSelectedItems: RealmResults<IgnoreRO>) {
        listItem.forEach { app ->
            if (app is AppData) {
                listSelectedItems.forEach {
                    if (app.packageName == it.packageName) {
                        selectedItems.put(listItem.indexOf(app), true)
                    }
                }
            }
        }
    }

    fun getItem(): ArrayList<AppData> {
        val listTemp = arrayListOf<AppData>()
        listItem.forEach {
            if (it is AppData) listTemp.add(it)
        }
        return listTemp
    }
}