package com.imaec.notificationhelper.base

import android.content.Intent
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
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.ui.view.dialog.ProgressDialog

abstract class BaseFragment<T : ViewDataBinding>(@LayoutRes private val layoutResId: Int) : Fragment() {

    protected val TAG = this::class.java.simpleName

    protected lateinit var binding: T
    private lateinit var interstitialAd: InterstitialAd
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var firebaseCrashlytics: FirebaseCrashlytics

    private val progressDialog: ProgressDialog by lazy { ProgressDialog(context!!) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        firebaseCrashlytics.log("$TAG onViewCreated")
        firebaseCrashlytics.setCustomKey(getString(R.string.key_fragment), TAG)
        logEvent(getString(R.string.event_screen_fragment), Bundle().apply {
            putString(getString(R.string.key_screen_fragment), TAG)
        })
    }

    override fun onResume() {
        super.onResume()
        firebaseCrashlytics.log("$TAG onResume")
    }

    override fun onPause() {
        super.onPause()
        firebaseCrashlytics.log("$TAG onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        firebaseCrashlytics.log("$TAG onDestroy")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        firebaseCrashlytics.log("$TAG onActivityResult")
    }

    protected fun showProgress() {
        if (!progressDialog.isShowing) progressDialog.show()
    }

    protected fun hideProgress() {
        if (progressDialog.isShowing) progressDialog.dismiss()
    }

    protected fun logEvent(key: String, bundle: Bundle) {
        firebaseAnalytics.logEvent(key, bundle)
    }

    private fun init() {
        MobileAds.initialize(context) {}
        firebaseAnalytics = Firebase.analytics
        firebaseCrashlytics = Firebase.crashlytics
    }
}