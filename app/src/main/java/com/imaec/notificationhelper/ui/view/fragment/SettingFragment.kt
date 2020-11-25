package com.imaec.notificationhelper.ui.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.imaec.notificationhelper.Extensions.getViewModel
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.base.BaseFragment
import com.imaec.notificationhelper.databinding.FragmentSettingBinding
import com.imaec.notificationhelper.model.AppData
import com.imaec.notificationhelper.repository.NotificationRepository
import com.imaec.notificationhelper.viewmodel.SettingViewModel

class SettingFragment : BaseFragment<FragmentSettingBinding>(R.layout.fragment_setting) {

    private lateinit var settingViewModel: SettingViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) return

        settingViewModel.getIgnore()
    }

    private fun init() {
        settingViewModel = getViewModel { SettingViewModel(NotificationRepository(context!!)) }

        binding.apply {
            lifecycleOwner = this@SettingFragment
            settingViewModel = this@SettingFragment.settingViewModel
            recyclerSetting.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        }

        getAppList()
    }

    private fun getAppList() {
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        val listApp = ArrayList<AppData>()
        context!!.packageManager.queryIntentActivities(intent, 0).forEach { app ->
            listApp.add(AppData(
                icon = context!!.packageManager.getApplicationIcon(app.activityInfo.packageName),
                packageName = app.activityInfo.packageName,
                name = app.activityInfo.loadLabel(context!!.packageManager).toString()
            ))
        }
        listApp.sortBy { it.name }
        settingViewModel.apply {
            setListApp(listApp)
            getIgnore()
        }
    }
}