package com.imaec.notificationhelper.activity

import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.forEach
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.adapter.AppAdapter
import com.imaec.notificationhelper.model.AppData
import com.imaec.notificationhelper.model.IgnoreRO
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    private lateinit var realm: Realm
    private lateinit var adapter: AppAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var listApps: List<ResolveInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        init()

        getAppList()
    }

    override fun onBackPressed() {
        val realmResult = realm.where(IgnoreRO::class.java).findAll()
        realm.executeTransaction {
            realmResult.deleteAllFromRealm()
            adapter.getSelectedItem().forEach { key, value ->
                it.createObject(IgnoreRO::class.java).apply {
                    ignoreIndex = key
                    packageName = listApps[key].activityInfo.packageName
                }
            }
        }
        setResult(100)
        finish()
    }

    private fun init() {
        realm = Realm.getDefaultInstance()
        adapter = AppAdapter(Glide.with(this))
        layoutManager = LinearLayoutManager(this)

        recyclerSetting.adapter = adapter
        recyclerSetting.layoutManager = layoutManager
        recyclerSetting.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
    }

    private fun getAppList() {
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        listApps = packageManager.queryIntentActivities(intent, 0)
        val realmResult = realm.where(IgnoreRO::class.java).findAll()
        val listSelectedItems = ArrayList<Int>()
        realmResult.forEach {
            listSelectedItems.add(it.ignoreIndex)
        }
        adapter.setSelectedItems(listSelectedItems)

        for (app in listApps) {
            val item = AppData().apply {
                icon = packageManager.getApplicationIcon(app.activityInfo.packageName)
                name = app.activityInfo.loadLabel(packageManager).toString()
            }
            adapter.addItem(item)
        }
        adapter.sort()
        adapter.notifyDataSetChanged()
    }
}