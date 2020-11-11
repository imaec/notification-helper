package com.imaec.notificationhelper

import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.imaec.notificationhelper.base.BaseAdapter
import com.imaec.notificationhelper.utils.Utils

object BindingAdapters {

    private val TAG = this::class.java.simpleName

    @JvmStatic
    @BindingAdapter("app:isVisible")
    fun isVisible(view: View, isVisible: Boolean) {
        view.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("imgUrl")
    fun setImgUrl(imageView: ImageView, imgUrl: String) {
        Glide.with(imageView)
            .load(imgUrl)
            .error(R.mipmap.ic_launcher)
            .into(imageView)
    }

    @JvmStatic
    @BindingAdapter("image")
    fun setImage(imageView: ImageView, packageName: String) {
        Glide.with(imageView)
            .load(Utils.getAppIcon(imageView.context, packageName) ?: ContextCompat.getDrawable(imageView.context, R.mipmap.ic_launcher))
            .into(imageView)
    }

    @JvmStatic
    @BindingAdapter("image")
    fun setImage(imageView: ImageView, bytes: ByteArray?) {
        val bitmap = Utils.getBitmap(bytes)
        Glide.with(imageView)
            .load(bitmap ?: 0)
            .into(imageView)
    }

    @JvmStatic
    @BindingAdapter("adapter")
    fun setAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        recyclerView.adapter = adapter
    }

    @JvmStatic
    @BindingAdapter("items")
    fun setItems(recyclerView: RecyclerView, items: List<Any>) {
        (recyclerView.adapter as BaseAdapter).apply {
            clearItem()
            addItems(items)
            notifyDataSetChanged()
        }
    }
}