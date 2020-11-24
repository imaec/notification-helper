package com.imaec.notificationhelper.ui.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.base.BaseDialog
import com.imaec.notificationhelper.databinding.DialogNotificationBinding

class NotificationDialog(context: Context) : BaseDialog<DialogNotificationBinding>(context, R.layout.dialog_notification) {

    private var title: String? = null
    private var onClickOpen: ((NotificationDialog) -> Unit)? = null
    private var onClickIgnore: ((NotificationDialog) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val size = Point()
        window?.windowManager?.defaultDisplay?.getSize(size)
        window?.apply {
            attributes = WindowManager.LayoutParams().apply {
                copyFrom(window?.attributes)
                width = (size.x * 0.8).toInt()
                height = WindowManager.LayoutParams.WRAP_CONTENT
            }
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        binding.apply {
            textTitle.text = title ?: context.getString(R.string.app_name)
            textOpen.setOnClickListener {
                onClickOpen?.let {
                    it(this@NotificationDialog)
                } ?: dismiss()
            }
            textIgnore.setOnClickListener {
                onClickIgnore?.let {
                    it(this@NotificationDialog)
                } ?: dismiss()
            }
        }
    }

    fun setTitle(title: String): NotificationDialog {
        this.title = title
        return this
    }

    fun setOnClickOpen(onClickOpen: (NotificationDialog) -> Unit): NotificationDialog {
        this.onClickOpen = onClickOpen
        return this
    }

    fun setOnClickIgnore(onClickIgnore: (NotificationDialog) -> Unit): NotificationDialog {
        this.onClickIgnore = onClickIgnore
        return this
    }
}