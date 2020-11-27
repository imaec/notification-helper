package com.imaec.notificationhelper.base

import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.ui.view.dialog.ProgressDialog

abstract class BaseActivity<T : ViewDataBinding>(@LayoutRes private val layoutResId: Int) : AppCompatActivity() {

    protected val TAG = this::class.java.simpleName

    protected lateinit var binding: T
    protected lateinit var interstitialAd: InterstitialAd
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var firebaseCrashlytics: FirebaseCrashlytics

    private val progressDialog: ProgressDialog by lazy { ProgressDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layoutResId)

        init()

        firebaseCrashlytics.log("$TAG onCreate")
        firebaseCrashlytics.setCustomKey(getString(R.string.key_activity), TAG)
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
        MobileAds.initialize(this) {}
        firebaseAnalytics = Firebase.analytics
        firebaseCrashlytics = Firebase.crashlytics
    }
}