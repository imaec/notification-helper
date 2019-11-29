package com.imaec.notificationhelper.fragment

import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.adapter.AppAdapter
import com.imaec.notificationhelper.model.AppData
import com.imaec.notificationhelper.model.IgnoreRO
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : Fragment() {

    private lateinit var realm: Realm
    private lateinit var adapter: AppAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var listApps: List<ResolveInfo>

    private var callback = object : IgnoreCallback {
        override fun onIgnore(position: Int, isSelected: Boolean) {
            val listItem = adapter.getItem()
            realm.executeTransaction {
                val realmResult = realm.where(IgnoreRO::class.java)
                    .equalTo("packageName", listItem[position].packageName)
                    .findFirst()
                if (isSelected) {
                    // 저장
                    it.createObject(IgnoreRO::class.java).apply {
                        packageName = listItem[position].packageName
                    }
                } else {
                    // 삭제
                    realmResult?.deleteFromRealm()
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_setting, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        getAppList()
    }

    private fun init() {
        realm = Realm.getDefaultInstance()
        adapter = AppAdapter(Glide.with(this), callback)
        layoutManager = LinearLayoutManager(context)

        recyclerSetting.adapter = adapter
        recyclerSetting.layoutManager = layoutManager
        recyclerSetting.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))
    }

    private fun getAppList() {
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        listApps = context!!.packageManager.queryIntentActivities(intent, 0)
        val realmResult = realm.where(IgnoreRO::class.java).findAll()

        for (app in listApps) {
            val item = AppData().apply {
                icon = context!!.packageManager.getApplicationIcon(app.activityInfo.packageName)
                packageName = app.activityInfo.packageName
                name = app.activityInfo.loadLabel(context!!.packageManager).toString()
            }
            adapter.addItem(item)
        }
        adapter.sort()
        adapter.setSelectedItems(realmResult)
        adapter.notifyDataSetChanged()
    }

    interface IgnoreCallback {
        fun onIgnore(position: Int, isSelected: Boolean)
    }
}