package com.imaec.notificationhelper.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.ads.*
import com.imaec.notificationhelper.BuildConfig
import com.imaec.notificationhelper.EndlessRecyclerOnScrollListener
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.adapter.DetailAdapter
import com.imaec.notificationhelper.model.ContentData
import com.imaec.notificationhelper.model.ContentRO
import com.imaec.notificationhelper.model.NotificationRO
import io.realm.Realm
import io.realm.Sort
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class DetailActivity : AppCompatActivity() {

    private val TAG = this::class.java.simpleName

    private lateinit var interstitialAd: InterstitialAd
    private lateinit var realm: Realm
    private lateinit var adapter: DetailAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private val listItem = ArrayList<ContentRO>()
    private var currentPage = 1
    private val count = 30
    private var i = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        adInit()

        init()

        getData()

        recyclerDetail.addOnScrollListener(object : EndlessRecyclerOnScrollListener(layoutManager) {
            override fun onLoadMore(current_page: Int) {
                currentPage = current_page + 1
                getData()
            }
        })
    }

    private fun adInit() {
        interstitialAd = InterstitialAd(this).apply {
            adUnitId = getString(R.string.ad_id_detail_front)
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    interstitialAd.show()
                    linearProgress.visibility = View.GONE
                }

                override fun onAdFailedToLoad(p0: Int) {
                    Log.d(TAG, "    ## error : $p0")
                    linearProgress.visibility = View.GONE
                    super.onAdFailedToLoad(p0)
                }

                override fun onAdFailedToLoad(p0: LoadAdError?) {
                    Log.d(TAG, "    ## error : $p0")
                    linearProgress.visibility = View.GONE
                    super.onAdFailedToLoad(p0)
                }
            }
        }
        adDetail.loadAd(AdRequest.Builder().build())
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
        showAd()
        if (listItem.size == 0) {
            val packageName = intent.getStringExtra("packageName")
            adapter.setPackageName(packageName)
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

    private fun showAd() {
        Random().let {
            val ran = it.nextInt(9) + 1
            if (i == ran) {
                interstitialAd.loadAd(AdRequest.Builder().build())
            } else {
                linearProgress.visibility = View.GONE
            }
        }
    }
}