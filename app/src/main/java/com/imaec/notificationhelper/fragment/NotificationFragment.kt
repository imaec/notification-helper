package com.imaec.notificationhelper.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.adapter.NotificationAdapter
import com.imaec.notificationhelper.model.IgnoreRO
import com.imaec.notificationhelper.model.NotificationRO
import io.realm.Realm
import io.realm.Sort
import kotlinx.android.synthetic.main.fragment_notification.*

class NotificationFragment : Fragment() {

    private lateinit var realm: Realm
    private lateinit var adapter: NotificationAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_notification, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    override fun onResume() {
        super.onResume()

        getData()
    }

    private fun init() {
        realm = Realm.getDefaultInstance()
        adapter = NotificationAdapter(Glide.with(this))
        layoutManager = LinearLayoutManager(context)

        recyclerNotification.adapter = adapter
        recyclerNotification.layoutManager = layoutManager
        recyclerNotification.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))
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
                linearNotificationEmpty.visibility = View.GONE
                adapter.addItem(it)
            }
        }
        adapter.notifyDataSetChanged()
    }
}