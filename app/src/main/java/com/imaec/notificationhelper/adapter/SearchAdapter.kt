package com.imaec.notificationhelper.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.TYPE_CONTENT
import com.imaec.notificationhelper.TYPE_NAME
import com.imaec.notificationhelper.Utils
import com.imaec.notificationhelper.activity.DetailActivity
import com.imaec.notificationhelper.activity.ImageActivity
import com.imaec.notificationhelper.model.ContentRO
import com.imaec.notificationhelper.model.NotificationRO
import kotlinx.android.synthetic.main.item_detail.view.*
import kotlinx.android.synthetic.main.item_notification.view.*
import java.text.SimpleDateFormat

class SearchAdapter(var glide: RequestManager) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context

    private val listItem = ArrayList<Any>()

    override fun getItemViewType(position: Int): Int {
        return when (listItem[position]) {
            is NotificationRO -> TYPE_NAME
            is ContentRO -> TYPE_CONTENT
            else -> TYPE_NAME
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        this.context = parent.context
        val view: View
        val vh: RecyclerView.ViewHolder
        when (viewType) {
            TYPE_NAME -> {
                view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false)
                vh = NameViewHolder(view)
            }
            TYPE_CONTENT -> {
                view = LayoutInflater.from(context).inflate(R.layout.item_detail, parent, false)
                vh = ContentViewHolder(view)
            }
            else -> {
                view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false)
                vh = NameViewHolder(view)
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

    inner class NameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imageIcon by lazy { itemView.image_item_icon }
        private val textName by lazy { itemView.text_item_name }

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

    inner class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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
            } ?: run {
                glide
                    .load(0)
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

    fun addItem(item: Any) {
        listItem.add(item)
    }

    fun clearItem() {
        listItem.clear()
    }
}