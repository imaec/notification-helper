package com.imaec.notificationhelper.ui.view.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.imaec.notificationhelper.Extensions.getViewModel
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.base.BaseFragment
import com.imaec.notificationhelper.databinding.FragmentNotificationBinding
import com.imaec.notificationhelper.model.NotificationRO
import com.imaec.notificationhelper.repository.NotificationRepository
import com.imaec.notificationhelper.ui.view.activity.DetailActivity
import com.imaec.notificationhelper.ui.view.activity.GroupDetailActivity
import com.imaec.notificationhelper.ui.view.dialog.IgnoreInfoDialog
import com.imaec.notificationhelper.ui.view.dialog.NotificationDialog
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
                        putExtra("packageName", item.packageName)
                    })
                }
            }
            addOnLongClickListener { item ->
                if (item is NotificationRO) {
                    NotificationDialog(context!!)
                        .setTitle(item.appName)
                        .setOnClickOpen {
                            activity?.packageManager?.getLaunchIntentForPackage(item.packageName)?.let { launchIntent ->
                                startActivity(launchIntent)
                            }
                            it.dismiss()
                        }
                        .setOnClickIgnore {
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
        IgnoreInfoDialog(context!!)
            .setContent(String.format(context!!.getString(R.string.msg_ignore_info, appName)))
            .setPositive(context!!.getString(R.string.ok)) {
                it.dismiss()
            }
            .setNegative(context!!.getString(R.string.not_show_again)) {
                it.dismiss()
            }
            .show()
    }
}