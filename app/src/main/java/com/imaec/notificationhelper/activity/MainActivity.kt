package com.imaec.notificationhelper.activity

import android.app.Activity
import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.imaec.notificationhelper.ACTION_NOTIFICATION
import com.imaec.notificationhelper.R
import kotlinx.android.synthetic.main.activity_main.*
import com.imaec.notificationhelper.fragment.NotificationFragment
import com.imaec.notificationhelper.fragment.SearchFragment
import com.imaec.notificationhelper.fragment.SettingFragment
import com.imaec.notificationhelper.service.NotificationHelperService

class MainActivity : AppCompatActivity() {

    private lateinit var transaction: FragmentTransaction
    private lateinit var fragmentNotification: NotificationFragment
    private lateinit var fragmentSearch: SearchFragment
    private lateinit var fragmentSetting: SettingFragment

    private var notificationReceiver: BroadcastReceiver? = null
    private val itemIds = listOf(R.id.navigation_notification, R.id.navigation_search, R.id.navigation_more)
    private val icons = listOf(R.mipmap.ic_notification_list, R.mipmap.ic_search, R.mipmap.ic_more)
    private val icons2 = listOf(R.mipmap.ic_notification_list, R.mipmap.ic_search, R.mipmap.ic_more)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

        bottomMain.setOnNavigationItemSelectedListener {
            transaction = supportFragmentManager.beginTransaction()
            when (it.itemId) {
                R.id.navigation_notification -> {
                    transaction.replace(R.id.frame_layout, fragmentNotification).commit()
                    setBottomIcon(it.itemId)
                }
                R.id.navigation_search -> {
                    transaction.replace(R.id.frame_layout, fragmentSearch).commit()
                    setBottomIcon(it.itemId)
                }
                R.id.navigation_more -> {
                    transaction.replace(R.id.frame_layout, fragmentSetting).commit()
                    setBottomIcon(it.itemId)
                }
            }
            true
        }
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

    private fun init() {
        bottomMain.itemIconTintList = null
        transaction = supportFragmentManager.beginTransaction()
        fragmentNotification = NotificationFragment()
        fragmentSearch = SearchFragment()
        fragmentSetting = SettingFragment()

        transaction.replace(R.id.frame_layout, fragmentNotification).commit()
        setBottomIcon(R.id.navigation_notification)
    }

    private fun setBottomIcon(itemId: Int) {
        // BottomNavigationView Icon Setting
        for ((i, id) in itemIds.withIndex()) {
            if (id == itemId) {
                bottomMain.menu.findItem(id).icon = resources.getDrawable(icons2[i])
            } else {
                bottomMain.menu.findItem(id).icon = resources.getDrawable(icons[i])
            }
        }
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
                setTitle("안내")
                setMessage("알림리스트를 사용하시려면\n'알림 접근 권한'을 '허용'하셔야 합니다.\n'${getString(R.string.app_name)}'의 알림 접근을 허용해주세요.")
                setNegativeButton("취소") { dialog, which ->
                    dialog.dismiss()
                    this@MainActivity.finish()
                }
                setPositiveButton("확인") { dialog, which ->
                    dialog.dismiss()
                    startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
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
            Log.d("servicename :::: ", service.service.className)
            if ("${packageName}.NotificationHelperService" == service.service.className) {
                isServiceRunning = true
            }
        }

        if (!isServiceRunning) {
            Log.d("isRunning :::: ", "false")
            val intent = Intent(this, NotificationHelperService::class.java)
            startService(intent)
        } else {
            Log.d("isRunning :::: ", "true")
        }
    }
}
