package com.imaec.notificationhelper.ui.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.WindowManager
import android.widget.FrameLayout
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.base.BaseDialog
import com.imaec.notificationhelper.databinding.DialogContentBinding
import com.imaec.notificationhelper.utils.Utils

class ContentDialog(context: Context) : BaseDialog<DialogContentBinding>(
    context,
    R.layout.dialog_content
) {

    constructor(context: Context, title: String, content: String, positive: String = "") : this(
        context
    ) {
        this.title = title
        this.content = content
        this.positive = positive
    }

    private var title: String? = null
    private var content: String? = null
    private var positive: String? = null
    private var onClickOk: ((ContentDialog) -> Unit)? = null

    private var maxHeight = 0
    private var isDraw = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val size = Point()
        window?.windowManager?.defaultDisplay?.getSize(size)
        window?.apply {
            attributes = WindowManager.LayoutParams().apply {
                copyFrom(this)
                width = (size.x * 0.8).toInt()
                gravity = Gravity.CENTER
            }
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            maxHeight = (size.y * 0.7).toInt()
        }

        binding.apply {
            textTitle.text = title ?: context.getString(R.string.info)
            textContent.text = content ?: throw NullPointerException("Title is Null")
            textOk.text = positive ?: textOk.text
            textOk.setOnClickListener {
                onClickOk?.let {
                    it(this@ContentDialog)
                } ?: dismiss()
            }

            layoutRoot.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val dialogHeight = textTitle.measuredHeight + textContent.measuredHeight + textOk.measuredHeight + Utils.dp(context, 44)
                    val params = if (dialogHeight > maxHeight) {
                        layoutRoot.layoutParams.apply {
                            height = maxHeight
                        }
                    } else {
                        layoutRoot.layoutParams.apply {
                            height = dialogHeight
                        }
                    } as FrameLayout.LayoutParams

                    params.gravity = Gravity.CENTER
                    layoutRoot.layoutParams = params
                    layoutRoot.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    isDraw = true
                }
            })
        }
    }

    fun setTitle(title: String): ContentDialog {
        this.title = title
        return this
    }

    fun setContent(content: String): ContentDialog {
        this.content = content
        return this
    }

    fun setPositive(positive: String, onClickOk: (ContentDialog) -> Unit): ContentDialog {
        this.positive = positive
        this.onClickOk = onClickOk
        return this
    }
}