package com.imaec.notificationhelper.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.imaec.notificationhelper.EndlessRecyclerOnScrollListener
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.adapter.DetailAdapter
import com.imaec.notificationhelper.model.ContentData
import com.imaec.notificationhelper.model.ContentRO
import com.imaec.notificationhelper.model.NotificationRO
import io.realm.Realm
import io.realm.Sort
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var realm: Realm
    private lateinit var adapter: DetailAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private val listItem = ArrayList<ContentRO>()
    private var currentPage = 1
    private val count = 30

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        init()

        getData()

        recyclerDetail.addOnScrollListener(object : EndlessRecyclerOnScrollListener(layoutManager) {
            override fun onLoadMore(current_page: Int) {
                currentPage = current_page + 1
                getData()
            }
        })
    }

    private fun init() {
        realm = Realm.getDefaultInstance()
        adapter = DetailAdapter(Glide.with(this))
        layoutManager = LinearLayoutManager(this)

        recyclerDetail.adapter = adapter
        recyclerDetail.layoutManager = layoutManager
        recyclerDetail.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
    }

    private fun getData() {
        if (listItem.size == 0) {
            val packageName = intent.getStringExtra("packageName")
            val realmResult = realm.where(NotificationRO::class.java)
                .equalTo("packageName", packageName)
                .findFirst()
            realmResult?.let {
                listItem.addAll(it.contents)
            }
            listItem.reverse()
        }

        for (i in (currentPage-1)*count until currentPage*count) {
            if (listItem.size > i) adapter.addItem(listItem[i])
        }
        adapter.notifyDataSetChanged()
    }
}