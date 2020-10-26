package com.imaec.notificationhelper.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.imaec.notificationhelper.ui.view.dialog.ProgressDialog

abstract class BaseFragment<T : ViewDataBinding>(@LayoutRes private val layoutResId: Int) : Fragment() {

    protected val TAG = this::class.java.simpleName

    protected lateinit var binding: T
    private lateinit var interstitialAd: InterstitialAd

    private val progressDialog: ProgressDialog by lazy { ProgressDialog(context!!) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    protected fun showProgress() {
        if (!progressDialog.isShowing) progressDialog.show()
    }

    protected fun hideProgress() {
        if (progressDialog.isShowing) progressDialog.dismiss()
    }

    private fun init() {
        MobileAds.initialize(context) {}
    }
}