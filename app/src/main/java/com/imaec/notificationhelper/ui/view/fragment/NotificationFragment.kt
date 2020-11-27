package com.imaec.notificationhelper.ui.view.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.imaec.notificationhelper.Extensions.getViewModel
import com.imaec.notificationhelper.ExtraKey
import com.imaec.notificationhelper.PrefKey
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.base.BaseFragment
import com.imaec.notificationhelper.databinding.FragmentNotificationBinding
import com.imaec.notificationhelper.model.NotificationRO
import com.imaec.notificationhelper.repository.NotificationRepository
import com.imaec.notificationhelper.ui.view.activity.DetailActivity
import com.imaec.notificationhelper.ui.view.activity.GroupDetailActivity
import com.imaec.notificationhelper.ui.view.dialog.CommonDialog
import com.imaec.notificationhelper.ui.view.dialog.NotificationDialog
import com.imaec.notificationhelper.utils.PreferencesUtil
import com.imaec.notificationhelper.viewmodel.NotificationViewModel

class NotificationFragment : BaseFragment<FragmentNotificationBinding>(R.layout.fragment_notification) {

    private lateinit var notificationViewModel: NotificationViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) return

        notificationViewModel.getNotifications()
    }

    private fun init() {
        notificationViewModel = getViewModel { NotificationViewModel(NotificationRepository(context!!)) }

        binding.apply {
            lifecycleOwner = this@NotificationFragment
            notificationViewModel = this@NotificationFragment.notificationViewModel
            recyclerNotification.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        }

        notificationViewModel.apply {
            getNotifications()
            addOnClickListener { item ->
                if (item is NotificationRO) {
                    startActivity(Intent(context, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        GroupDetailActivity::class.java
                    } else {
                        DetailActivity::class.java
                    }).apply {
                        putExtra(ExtraKey.EXTRA_PACKAGE_NAME, item.packageName)
                    })
                }
            }
            addOnLongClickListener { item ->
                if (item is NotificationRO) {
                    NotificationDialog(context!!)
                        .setTitle(if (item.appName.isNotEmpty()) item.appName else getString(R.string.system))
                        .setOnClickOpen {
                            activity?.packageManager?.getLaunchIntentForPackage(item.packageName)?.let { launchIntent ->
                                startActivity(launchIntent)
                            }
                            it.dismiss()
                        }
                        .setOnClickDelete {
                            showDeleteInfo(item)
                            it.dismiss()
                        }
                        .setOnClickIgnore {
                            if (item.appName.isEmpty()) {
                                Toast.makeText(context, R.string.msg_cannot_ignore_system_alert, Toast.LENGTH_SHORT).show()
                                it.dismiss()
                                return@setOnClickIgnore
                            }
                            notificationViewModel.apply {
                                setIgnore(item.packageName)
                                getNotifications()

                                showIgnoreInfo(item.appName)
                            }
                            it.dismiss()
                        }
                        .show()
                }
            }
        }
    }

    private fun showIgnoreInfo(appName: String) {
        if (PreferencesUtil.getBool(context!!, PrefKey.PREF_NOT_SHOW_AGAIN)) return

        CommonDialog(context!!)
            .setContent(String.format(context!!.getString(R.string.msg_ignore_info, appName)))
            .setPositive(context!!.getString(R.string.ok)) {
                it.dismiss()
            }
            .setNegative(context!!.getString(R.string.not_show_again)) {
                PreferencesUtil.put(context!!, PrefKey.PREF_NOT_SHOW_AGAIN, true)
                it.dismiss()
            }
            .show()
    }

    private fun showDeleteInfo(item: NotificationRO) {
        CommonDialog(context!!)
            .setContent(String.format(context!!.getString(R.string.msg_delete_all, item.appName)))
            .setPositive(context!!.getString(R.string.delete)) {
                notificationViewModel.delete(item.packageName)
                it.dismiss()
            }
            .setNegative(context!!.getString(R.string.cancel)) {
                it.dismiss()
            }
            .show()
    }
}