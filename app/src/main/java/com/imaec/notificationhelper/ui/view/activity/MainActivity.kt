package com.imaec.notificationhelper.ui.view.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.imaec.notificationhelper.ACTION_NOTIFICATION
import com.imaec.notificationhelper.ActivityRequestCode
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.base.BaseActivity
import com.imaec.notificationhelper.databinding.ActivityMainBinding
import com.imaec.notificationhelper.ui.view.fragment.NotificationFragment
import com.imaec.notificationhelper.ui.view.fragment.SearchFragment
import com.imaec.notificationhelper.ui.view.fragment.SettingFragment
import com.imaec.notificationhelper.service.NotificationHelperService
import com.imaec.notificationhelper.utils.BackPressHandler

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    companion object {
        val adLoaded = MutableLiveData(false)
    }

    private lateinit var backPressHandler: BackPressHandler
    private var fragmentNotification = NotificationFragment()
    private var fragmentSearch = SearchFragment()
    private var fragmentSetting = SettingFragment()
    private var activeFragment: Fragment = fragmentNotification

    private var notificationReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adInit()

        startActivityForResult(Intent(this, SplashActivity::class.java), ActivityRequestCode.REQUEST_SPLASH_FINISH)

        init()
    }

    override fun onResume() {
        super.onResume()

        register()
        reStartAction()
        // reStartService()
    }

    override fun onPause() {
        super.onPause()
        unregister()
    }

    override fun onBackPressed() {
        backPressHandler.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ActivityRequestCode.REQUEST_SPLASH_FINISH) {
            if (resultCode == RESULT_OK) {
                interstitialAd.show()
                adLoaded.value = false
            }
        }
    }

    private fun adInit() {
        interstitialAd = InterstitialAd(this).apply {
            adUnitId = getString(R.string.ad_id_splash_front)
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    adLoaded.value = true
                }
            }
        }
        interstitialAd.loadAd(AdRequest.Builder().build())
        binding.adMain.loadAd(AdRequest.Builder().build())
    }

    @SuppressLint("Recycle")
    private fun init() {
        binding.apply {
            lifecycleOwner = this@MainActivity
            bottomMain.apply {
                // BottomNavigationView Icon Setting
                resources.getIntArray(R.array.tab_ids).forEachIndexed { index, _ ->
                    val id = resources.obtainTypedArray(R.array.tab_ids).getResourceId(index, -1)
                    val icon = resources.obtainTypedArray(R.array.tab_icons).getResourceId(index, -1)
                    menu.findItem(id).setIcon(icon)
                }
                itemIconTintList = null
                setOnNavigationItemSelectedListener {
                    setFragment(when (it.itemId) {
                        R.id.navigation_notification -> fragmentNotification
                        R.id.navigation_search -> fragmentSearch
                        R.id.navigation_more -> fragmentSetting
                        else -> fragmentNotification
                    })
                    true
                }
            }
        }

        supportFragmentManager.beginTransaction().add(R.id.frame, fragmentSetting, getString(R.string.setting)).hide(fragmentSetting).commitAllowingStateLoss()
        supportFragmentManager.beginTransaction().add(R.id.frame, fragmentSearch, getString(R.string.search)).hide(fragmentSearch).commitAllowingStateLoss()
        supportFragmentManager.beginTransaction().add(R.id.frame, fragmentNotification, getString(R.string.notification)).commitAllowingStateLoss()
        backPressHandler = BackPressHandler(this)
    }

    private fun register() {
        if (notificationReceiver != null) return

        val intentFilter = IntentFilter(ACTION_NOTIFICATION)
        notificationReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == ACTION_NOTIFICATION) {
                    // Notification을 받으면

                }
            }
        }

        registerReceiver(notificationReceiver, intentFilter)
    }

    private fun unregister() {
        if (notificationReceiver != null) {
            unregisterReceiver(notificationReceiver)
            notificationReceiver = null
        }
    }

    private fun reStartAction() {
        var isDenied = true
        repeat(NotificationManagerCompat.getEnabledListenerPackages(this)
            .filter { it == packageName }.size
        ) { isDenied = false }
        if (isDenied) {
            AlertDialog.Builder(this).apply {
                setTitle(R.string.info)
                setMessage(getString(R.string.msg_access_notification, getString(R.string.app_name)))
                setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                    this@MainActivity.finish()
                }
                setPositiveButton(R.string.ok) { dialog, _ ->
                    dialog.dismiss()
                    startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
                }
                create()
                show()
            }
        }
    }

    private fun reStartService() {
        var isServiceRunning = false
        val manager = this.getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            Log.d(TAG, "    ## serviceName : ${service.service.className}")
            if ("${packageName}.NotificationHelperService" == service.service.className) {
                isServiceRunning = true
            }
        }

        Log.d(TAG, "    ## isRunning : $isServiceRunning")
        if (!isServiceRunning) {
            val intent = Intent(this, NotificationHelperService::class.java)
            startService(intent)
        }
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .hide(activeFragment)
            .show(fragment)
            .commit()
        activeFragment = fragment
    }
}
