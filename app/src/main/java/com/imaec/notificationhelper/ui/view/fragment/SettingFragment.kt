package com.imaec.notificationhelper.ui.view.fragment

import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.ui.adapter.AppAdapter
import com.imaec.notificationhelper.base.BaseFragment
import com.imaec.notificationhelper.databinding.FragmentSettingBinding
import com.imaec.notificationhelper.model.AppData
import com.imaec.notificationhelper.model.IgnoreRO
import io.realm.Realm

class SettingFragment : BaseFragment<FragmentSettingBinding>(R.layout.fragment_setting) {

    private val realm by lazy { Realm.getDefaultInstance() }
    private val adapter by lazy { AppAdapter(Glide.with(this), callback) }
    private val layoutManager = LinearLayoutManager(context)
    private lateinit var listApps: List<ResolveInfo>

    private val callback = object : IgnoreCallback {
        override fun onIgnore(position: Int, isSelected: Boolean) {
            setIgnore(position, isSelected)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        getAppList()
    }

    private fun init() {
        binding.apply {
            lifecycleOwner = this@SettingFragment
            recyclerSetting.adapter = adapter
            recyclerSetting.layoutManager = layoutManager
            recyclerSetting.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))
        }
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

    private fun setIgnore(position: Int, isSelected: Boolean) {
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

    interface IgnoreCallback {
        fun onIgnore(position: Int, isSelected: Boolean)
    }
}