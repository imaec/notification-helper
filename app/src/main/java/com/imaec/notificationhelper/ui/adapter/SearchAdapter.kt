package com.imaec.notificationhelper.ui.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.TYPE_CONTENT
import com.imaec.notificationhelper.TYPE_NAME
import com.imaec.notificationhelper.Utils
import com.imaec.notificationhelper.base.BaseAdapter
import com.imaec.notificationhelper.databinding.ItemDetailBinding
import com.imaec.notificationhelper.databinding.ItemNotificationBinding
import com.imaec.notificationhelper.ui.view.activity.DetailActivity
import com.imaec.notificationhelper.ui.view.activity.ImageActivity
import com.imaec.notificationhelper.model.ContentRO
import com.imaec.notificationhelper.model.NotificationRO
import java.text.SimpleDateFormat

class SearchAdapter(var glide: RequestManager) : BaseAdapter() {

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

        private val imageIcon by lazy { binding.root.findViewById<ImageView>(R.id.image_item_icon) }
        private val textName by lazy { binding.root.findViewById<TextView>(R.id.text_item_name) }

        @SuppressLint("SetTextI18n")
        fun onBind(item: NotificationRO) {
            glide
                .load(Utils.getAppIcon(binding.root.context, item.packageName))
                .into(imageIcon)
            textName.text = "${item.appName} (${item.contents.size})"

            itemView.setOnClickListener {
                val intent = Intent(binding.root.context, DetailActivity::class.java).apply {
                    putExtra("packageName", item.packageName)
                }
                binding.root.context.startActivity(intent)
            }
        }
    }

    inner class ContentViewHolder(val binding: ItemDetailBinding) : RecyclerView.ViewHolder(binding.root) {

        private val imageIcon by lazy { binding.root.findViewById<ImageView>(R.id.image_item_detail_icon) }
        private val textTitle by lazy { binding.root.findViewById<TextView>(R.id.text_item_title) }
        private val imageContent by lazy { binding.root.findViewById<ImageView>(R.id.image_item_content) }
        private val textContent by lazy { binding.root.findViewById<TextView>(R.id.text_item_content) }
        private val textTime by lazy { binding.root.findViewById<TextView>(R.id.text_item_time) }

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
                AlertDialog.Builder(binding.root.context).apply {
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
                binding.root.context.startActivity(Intent(binding.root.context, ImageActivity::class.java).apply {
                    putExtra("img", item.img2)
                })
            }
        }
    }
}