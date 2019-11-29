package com.imaec.notificationhelper.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.Utils
import com.imaec.notificationhelper.activity.ImageActivity
import com.imaec.notificationhelper.model.ContentData
import com.imaec.notificationhelper.model.ContentRO
import kotlinx.android.synthetic.main.item_detail.view.*
import java.text.SimpleDateFormat

class DetailAdapter(val glide: RequestManager) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context

    private val listItem = ArrayList<ContentRO>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_detail, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int = listItem.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.onBind(listItem[position])
        }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imageIcon by lazy { itemView.imageItemDetailIcon }
        private val textTitle by lazy { itemView.textItemTitle }
        private val imageContent by lazy { itemView.imageItemContent }
        private val textContent by lazy { itemView.textItemContent }
        private val textTime by lazy { itemView.textItemTime }

        @SuppressLint("SimpleDateFormat")
        fun onBind(item: ContentRO) {
            val bitmap = Utils.getBitmap(item.img)
            val bitmap2 = Utils.getBitmap(item.img2)
            bitmap?.let {
                glide
                    .load(bitmap)
                    .into(imageIcon)
            }
            bitmap2?.let {
                imageContent.visibility = View.VISIBLE
                glide
                    .load(bitmap2)
                    .into(imageContent)
            } ?: run {
                imageContent.visibility = View.GONE
            }
            textTitle.text = item.title
            textContent.text = item.content
            textTime.text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(item.pKey)

            itemView.setOnClickListener {
                AlertDialog.Builder(context).apply {
                    setTitle(item.title)
                    setMessage(item.content)
                    setPositiveButton("확인") { dialog, which ->
                        dialog.dismiss()
                    }
                    create()
                    show()
                }
            }

            imageContent.setOnClickListener {
                context.startActivity(Intent(context, ImageActivity::class.java).apply {
                    putExtra("img", item.img2)
                })
            }
        }
    }

    fun addItem(item: ContentRO) {
        listItem.add(item)
    }
}