package com.imaec.notificationhelper.ui.view.activity

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.imaec.notificationhelper.Extensions.getViewModel
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.base.BaseActivity
import com.imaec.notificationhelper.databinding.ActivityGroupDetailBinding
import com.imaec.notificationhelper.repository.NotificationRepository
import com.imaec.notificationhelper.viewmodel.DetailViewModel
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
            GroupDetailViewModel(NotificationRepository(this), intent.getStringExtra("packageName")!!)
        }

        binding.apply {
            lifecycleOwner = this@GroupDetailActivity
            groupViewModel = this@GroupDetailActivity.groupViewModel
            recyclerGroup.addItemDecoration(DividerItemDecoration(this@GroupDetailActivity, RecyclerView.VERTICAL))
        }

        groupViewModel.getData()
    }
}