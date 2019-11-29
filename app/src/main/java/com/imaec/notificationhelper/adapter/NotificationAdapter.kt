package com.imaec.notificationhelper.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.Utils
import com.imaec.notificationhelper.activity.DetailActivity
import com.imaec.notificationhelper.model.ContentData
import com.imaec.notificationhelper.model.ContentRO
import com.imaec.notificationhelper.model.NotificationData
import com.imaec.notificationhelper.model.NotificationRO
import io.realm.Realm
import kotlinx.android.synthetic.main.item_notification.view.*

class NotificationAdapter(val glide: RequestManager) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context

    private val listItem = ArrayList<NotificationRO>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int = listItem.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.onBind(listItem[position])
        }
    }

    inner class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val imageIcon by lazy { itemView.imageItemIcon }
        private val textName by lazy { itemView.textItemName }

        @SuppressLint("SetTextI18n")
        fun onBind(item: NotificationRO) {
            glide
                .load(Utils.getAppIcon(context, item.packageName))
                .into(imageIcon)
            textName.text = "${item.appName} (${item.contents.size})"

            itemView.setOnClickListener {
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra("packageName", item.packageName)
                }
                context.startActivity(intent)
            }
        }
    }

    fun addItem(item: NotificationRO) {
        listItem.add(item)
    }

    fun clearItem() {
        listItem.clear()
    }
}