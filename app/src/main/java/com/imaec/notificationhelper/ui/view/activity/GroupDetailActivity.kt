package com.imaec.notificationhelper.ui.view.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.imaec.notificationhelper.Extensions.getViewModel
import com.imaec.notificationhelper.ExtraKey
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.base.BaseActivity
import com.imaec.notificationhelper.databinding.ActivityGroupDetailBinding
import com.imaec.notificationhelper.model.GroupDetailData
import com.imaec.notificationhelper.repository.NotificationRepository
import com.imaec.notificationhelper.ui.view.dialog.CommonDialog
import com.imaec.notificationhelper.ui.view.dialog.GroupDetailDialog
import com.imaec.notificationhelper.ui.view.dialog.NotificationDialog
import com.imaec.notificationhelper.viewmodel.GroupDetailViewModel

@RequiresApi(Build.VERSION_CODES.N)
class GroupDetailActivity : BaseActivity<ActivityGroupDetailBinding>(R.layout.activity_group_detail) {

    private lateinit var groupViewModel: GroupDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
    }

    private fun init() {
        groupViewModel = getViewModel {
            GroupDetailViewModel(NotificationRepository(this))
        }

        binding.apply {
            lifecycleOwner = this@GroupDetailActivity
            groupViewModel = this@GroupDetailActivity.groupViewModel
            recyclerGroup.addItemDecoration(DividerItemDecoration(this@GroupDetailActivity, RecyclerView.VERTICAL))
        }

        groupViewModel.apply {
            getData(intent.getStringExtra(ExtraKey.EXTRA_PACKAGE_NAME)!!)
            addOnClickListener { item ->
                if (item is GroupDetailData) {
                    startActivity(Intent(this@GroupDetailActivity, DetailActivity::class.java).apply {
                        putExtra(ExtraKey.EXTRA_PACKAGE_NAME, item.packageName)
                        putExtra(ExtraKey.EXTRA_TITLE, item.groupName)
                    })
                }
            }
            addOnLongClickListener { item ->
                if (item is GroupDetailData) {
                    GroupDetailDialog(this@GroupDetailActivity)
                        .setTitle(if (item.groupName.isNotEmpty()) item.groupName else getString(R.string.system))
                        .setOnClickDelete {
                            showDeleteInfo(item)
                            it.dismiss()
                        }
                        .show()
                }
            }
        }
    }

    private fun showDeleteInfo(item: GroupDetailData) {
        CommonDialog(this)
            .setContent(String.format(getString(R.string.msg_delete_group, item.groupName)))
            .setPositive(getString(R.string.delete)) {
                groupViewModel.delete(item.packageName)
                it.dismiss()
            }
            .setNegative(getString(R.string.cancel)) {
                it.dismiss()
            }
            .show()
    }
}