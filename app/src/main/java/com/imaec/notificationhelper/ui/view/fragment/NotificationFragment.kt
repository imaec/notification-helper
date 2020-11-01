package com.imaec.notificationhelper.ui.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.ui.view.activity.DetailActivity
import com.imaec.notificationhelper.ui.adapter.NotificationAdapter
import com.imaec.notificationhelper.base.BaseFragment
import com.imaec.notificationhelper.databinding.FragmentNotificationBinding
import com.imaec.notificationhelper.model.IgnoreRO
import com.imaec.notificationhelper.model.NotificationRO
import io.realm.Realm
import io.realm.Sort

class NotificationFragment : BaseFragment<FragmentNotificationBinding>(R.layout.fragment_notification) {

    private val realm by lazy { Realm.getDefaultInstance() }
    private val adapter by lazy { NotificationAdapter(Glide.with(this)) }
    private val layoutManager = LinearLayoutManager(context)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        adapter.addOnClickListener { item ->
            if (item is NotificationRO) {
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra("packageName", item.packageName)
                }
                startActivity(intent)
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) return

        getData()
    }

    private fun init() {
        binding.apply {
            lifecycleOwner = this@NotificationFragment
            recyclerNotification.adapter = adapter
            recyclerNotification.layoutManager = layoutManager
            recyclerNotification.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))
        }

        getData()
    }

    private fun getData() {
        val realmResult = realm.where(NotificationRO::class.java)
            .sort("saveTime", Sort.DESCENDING)
            .findAll()

        adapter.clearItem()
        realmResult.forEach {
            val ignore = realm.where(IgnoreRO::class.java)
                .equalTo("packageName", it.packageName)
                .findFirst()
            if (ignore == null) {
                binding.textEmptyNotification.visibility = View.GONE
                adapter.addItem(it)
            }
        }
        adapter.notifyDataSetChanged()
    }
}