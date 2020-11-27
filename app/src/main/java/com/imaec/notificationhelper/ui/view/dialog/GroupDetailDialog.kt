package com.imaec.notificationhelper.ui.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.base.BaseDialog
import com.imaec.notificationhelper.databinding.DialogGroupDetailBinding
import com.imaec.notificationhelper.databinding.DialogNotificationBinding

class GroupDetailDialog(context: Context) : BaseDialog<DialogGroupDetailBinding>(context, R.layout.dialog_group_detail) {

    private var title: String? = null
    private var onClickDelete: ((GroupDetailDialog) -> Unit)? = null

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
            textDelete.setOnClickListener {
                onClickDelete?.let {
                    it(this@GroupDetailDialog)
                } ?: dismiss()
            }
        }
    }

    fun setTitle(title: String): GroupDetailDialog {
        this.title = title
        return this
    }

    fun setOnClickDelete(onClickDelete: (GroupDetailDialog) -> Unit): GroupDetailDialog {
        this.onClickDelete = onClickDelete
        return this
    }
}