package com.imaec.notificationhelper.ui.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.ads.*
import com.imaec.notificationhelper.Extensions.getViewModel
import com.imaec.notificationhelper.ui.callback.EndlessRecyclerOnScrollListener
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.ui.adapter.DetailAdapter
import com.imaec.notificationhelper.base.BaseActivity
import com.imaec.notificationhelper.databinding.ActivityDetailBinding
import com.imaec.notificationhelper.model.ContentRO
import com.imaec.notificationhelper.model.NotificationRO
import com.imaec.notificationhelper.viewmodel.DetailViewModel
import io.realm.Realm
import java.util.*
import kotlin.collections.ArrayList

class DetailActivity : BaseActivity<ActivityDetailBinding>(R.layout.activity_detail) {

    private lateinit var detailViewModel: DetailViewModel

    private val realm by lazy { Realm.getDefaultInstance() }
    private val layoutManager = LinearLayoutManager(this)

    private val listItem = ArrayList<ContentRO>()
    private var currentPage = 1
    private val count = 30
    private var i = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adInit()

        init()
    }

    private fun adInit() {
        interstitialAd = InterstitialAd(this).apply {
            adUnitId = getString(R.string.ad_id_detail_front)
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    interstitialAd.show()
                    hideProgress()
                }

                override fun onAdFailedToLoad(p0: Int) {
                    Log.d(TAG, "    ## error : $p0")
                    hideProgress()
                    super.onAdFailedToLoad(p0)
                }

                override fun onAdFailedToLoad(p0: LoadAdError?) {
                    Log.d(TAG, "    ## error : $p0")
                    hideProgress()
                    super.onAdFailedToLoad(p0)
                }
            }
        }
        binding.adDetail.loadAd(AdRequest.Builder().build())
    }

    private fun init() {
        detailViewModel = getViewModel {
            DetailViewModel({ item, isImage ->
                if (isImage) {
                    startActivity(Intent(this, ImageActivity::class.java).apply {
                        putExtra("img", item.img2)
                    })
                } else {
                    android.app.AlertDialog.Builder(binding.root.context).apply {
                        setTitle(item.title)
                        setMessage(item.content)
                        setPositiveButton("확인") { dialog, which ->
                            dialog.dismiss()
                        }
                        create()
                        show()
                    }
                }
            })
        }

        binding.apply {
            lifecycleOwner = this@DetailActivity
            detailViewModel = this@DetailActivity.detailViewModel

            recyclerDetail.apply {
                addItemDecoration(DividerItemDecoration(this@DetailActivity, this@DetailActivity.layoutManager.orientation))
                addOnScrollListener(object : EndlessRecyclerOnScrollListener(this@DetailActivity.layoutManager) {
                    override fun onLoadMore(current_page: Int) {
                        currentPage = current_page + 1
                        getData()
                    }
                })
            }
        }

        showProgress()
        showAd()
        detailViewModel.getData()
    }

    private fun getData() {
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
                hideProgress()
            }
        }
    }
}