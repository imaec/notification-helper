package com.imaec.notificationhelper.ui.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.base.BaseDialog
import com.imaec.notificationhelper.databinding.DialogDeleteBinding

class DeleteDialog(context: Context) : BaseDialog<DialogDeleteBinding>(context, R.layout.dialog_delete) {

    private var title: String? = null
    private var onClickDelete: ((DeleteDialog) -> Unit)? = null

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
                    it(this@DeleteDialog)
                } ?: dismiss()
            }
        }
    }

    fun setTitle(title: String): DeleteDialog {
        this.title = title
        return this
    }

    fun setOnClickDelete(onClickDelete: (DeleteDialog) -> Unit): DeleteDialog {
        this.onClickDelete = onClickDelete
        return this
    }
}