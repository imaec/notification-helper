package com.imaec.notificationhelper.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseDialog<T : ViewDataBinding>(
    context: Context,
    @LayoutRes private val layoutResId: Int
) : Dialog(context) {

    protected var TAG = this::class.java.simpleName

    protected lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), layoutResId, null, false)
        setContentView(binding.root)
    }
}