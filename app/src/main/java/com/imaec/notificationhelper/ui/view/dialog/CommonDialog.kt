package com.imaec.notificationhelper.ui.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.base.BaseDialog
import com.imaec.notificationhelper.databinding.DialogCommonBinding

class CommonDialog(context: Context) : BaseDialog<DialogCommonBinding>(context, R.layout.dialog_common) {

    constructor(context: Context, content: String, positive: String = "", negative: String = "") : this(context) {
        this.content = content
        this.positive = positive
        this.negative = negative
    }

    private var content: String? = null
    private var positive: String? = null
    private var negative: String? = null
    private var onClickOk: ((CommonDialog) -> Unit)? = null
    private var onClickCancel: ((CommonDialog) -> Unit)? = null

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
            textContent.text = content ?: throw NullPointerException("Title is Null")
            textOk.text = positive ?: textOk.text
            textOk.setOnClickListener {
                onClickOk?.let {
                    it(this@CommonDialog)
                } ?: dismiss()
            }
            textCancel.text = negative ?: textCancel.text
            textCancel.setOnClickListener {
                onClickCancel?.let {
                    it(this@CommonDialog)
                } ?: dismiss()
            }
        }
    }

    fun setContent(content: String): CommonDialog {
        this.content = content
        return this
    }

    fun setPositive(positive: String, onClickOk: (CommonDialog) -> Unit): CommonDialog {
        this.positive = positive
        this.onClickOk = onClickOk
        return this
    }

    fun setNegative(negative: String, onClickCancel: (CommonDialog) -> Unit): CommonDialog {
        this.negative = negative
        this.onClickCancel = onClickCancel
        return this
    }
}