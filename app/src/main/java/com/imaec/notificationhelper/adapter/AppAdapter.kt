package com.imaec.notificationhelper.adapter

import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.fragment.SettingFragment
import com.imaec.notificationhelper.model.AppData
import com.imaec.notificationhelper.model.IgnoreRO
import io.realm.RealmResults
import kotlinx.android.synthetic.main.item_app.view.*

class AppAdapter(val glide: RequestManager, val callback: SettingFragment.IgnoreCallback) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context

    private var listItem = ArrayList<AppData>()
    private val selectedItems = SparseBooleanArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_app, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int = listItem.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.onBind(listItem[position], position)
        }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imageIcon by lazy { itemView.imageItemSettingIcon }
        private val textName by lazy { itemView.textItemSettingName }

        fun onBind(item: AppData, position: Int) {
            glide
                .load(item.icon)
                .into(imageIcon)

            textName.text = item.name

            if (selectedItems.get(position, false)) {
                itemView.setBackgroundColor(context.resources.getColor(android.R.color.white))
                textName.setTextColor(context.resources.getColor(R.color.colorPrimary))
            } else {
                itemView.setBackgroundColor(context.resources.getColor(R.color.colorPrimary))
                textName.setTextColor(context.resources.getColor(android.R.color.white))
            }

            itemView.setOnClickListener {
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
        val listTemp = listItem.sortedWith(compareBy { it.name })
        listItem.clear()
        listTemp.forEach {
            listItem.add(it)
        }
    }

    fun setSelectedItems(listSelectedItems: RealmResults<IgnoreRO>) {
        listItem.forEach { app ->
            listSelectedItems.forEach {
                if (app.packageName == it.packageName) {
                    selectedItems.put(listItem.indexOf(app), true)
                }
            }
        }
    }

    fun getItem(): ArrayList<AppData> {
        return listItem
    }
}