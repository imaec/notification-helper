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
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.imaec.notificationhelper.ACTION_NOTIFICATION
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.service.MyNotificationService
import kotlinx.android.synthetic.main.activity_main.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.imaec.notificationhelper.adapter.NotificationAdapter
import com.imaec.notificationhelper.model.IgnoreRO
import com.imaec.notificationhelper.model.NotificationRO
import io.realm.Realm
import io.realm.Sort

class MainActivity : AppCompatActivity() {

    private lateinit var realm: Realm
    private lateinit var adapter: NotificationAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private var notificationReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

        getData()

        fabMain.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivityForResult(intent, 0)
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.clearItem()
        getData()

        register()
        reStartAction()
        // reStartService()
    }

    override fun onPause() {
        super.onPause()
        unregister()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 100) {
            val realmResult = realm.where(IgnoreRO::class.java).findAll()
            for (result in realmResult) {
                Log.d("index :::: ", "${result.ignoreIndex}")
            }
        }
    }

    private fun init() {
        realm = Realm.getDefaultInstance()
        adapter = NotificationAdapter(Glide.with(this))
        layoutManager = LinearLayoutManager(this)

        recyclerMain.adapter = adapter
        recyclerMain.layoutManager = layoutManager
        recyclerMain.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
    }

    private fun register() {
        if (notificationReceiver != null) return

        val intentFilter = IntentFilter(ACTION_NOTIFICATION)
        notificationReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == ACTION_NOTIFICATION) {
                    // Notification을 받으면
                    adapter.clearItem()
                    getData()
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
        if (isDenied)
            startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
    }

    private fun reStartService() {
        var isServiceRunning = false
        val manager = this.getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            Log.d("servicename :::: ", service.service.className)
            if ("${packageName}.NotificationService" == service.service.className) {
                isServiceRunning = true
            }
        }

        if (!isServiceRunning) {
            Log.d("isRunning :::: ", "false")
            val intent = Intent(this, MyNotificationService::class.java)
            startService(intent)
        } else {
            Log.d("isRunning :::: ", "true")
        }
    }

    private fun getData() {
        val realmResult = realm.where(NotificationRO::class.java)
            .sort("saveTime", Sort.DESCENDING)
            .findAll()

        realmResult.forEach {
            adapter.addItem(it)
        }
        adapter.notifyDataSetChanged()
    }
}
