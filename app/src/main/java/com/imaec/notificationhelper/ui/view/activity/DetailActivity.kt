package com.imaec.notificationhelper.ui.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.LoadAdError
import com.imaec.notificationhelper.Extensions.getViewModel
import com.imaec.notificationhelper.ExtraKey
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.base.BaseActivity
import com.imaec.notificationhelper.databinding.ActivityDetailBinding
import com.imaec.notificationhelper.model.ContentRO
import com.imaec.notificationhelper.repository.NotificationRepository
import com.imaec.notificationhelper.ui.callback.EndlessRecyclerOnScrollListener
import com.imaec.notificationhelper.ui.view.dialog.CommonDialog
import com.imaec.notificationhelper.ui.view.dialog.DeleteDialog
import com.imaec.notificationhelper.utils.Utils
import com.imaec.notificationhelper.viewmodel.DetailViewModel
import java.util.*

class DetailActivity : BaseActivity<ActivityDetailBinding>(R.layout.activity_detail) {

    private lateinit var detailViewModel: DetailViewModel

    private val layoutManager = LinearLayoutManager(this)

    private var currentPage = 1
    private var i = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adInit()

        init()
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.image_back -> {
                onBackPressed()
            }
        }
    }

    private fun adInit() {
        interstitialAd = InterstitialAd(this).apply {
            adUnitId = getString(R.string.ad_id_detail_front)
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    interstitialAd.show()
                    hideProgress()
                }

                override fun onAdFailedToLoad(p0: LoadAdError?) {
                    Log.d(TAG, "    ## error : $p0")
                    hideProgress()
                    super.onAdFailedToLoad(p0)
                }

                override fun onAdClosed() {
                    super.onAdClosed()
                    getData()
                }
            }
        }
        binding.adDetail.loadAd(AdRequest.Builder().build())
    }

    private fun init() {
        detailViewModel = getViewModel {
            DetailViewModel(NotificationRepository(this)) { item, isImage ->
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
            }
        }

        binding.apply {
            lifecycleOwner = this@DetailActivity
            detailViewModel = this@DetailActivity.detailViewModel

            recyclerDetail.apply {
                addItemDecoration(DividerItemDecoration(this@DetailActivity, RecyclerView.VERTICAL))
                addOnScrollListener(object : EndlessRecyclerOnScrollListener(this@DetailActivity.layoutManager) {
                    override fun onLoadMore(current_page: Int) {
                        currentPage = current_page + 1
                    }
                })
            }
        }

        detailViewModel.apply {
            addOnLongClickListener { item ->
                if (item is ContentRO) {
                    DeleteDialog(this@DetailActivity)
                        .setTitle(if (item.title.isNotEmpty()) item.title else getString(R.string.system))
                        .setOnClickDelete {
                            showDeleteInfo(item)
                            it.dismiss()
                        }
                        .show()
                }
            }
        }

        showProgress()
        showAd()
    }

    private fun showAd() {
        Random().let {
            val ran = it.nextInt(4) + 1
            if (i == ran) {
                interstitialAd.loadAd(AdRequest.Builder().build())
            } else {
                getData()
                hideProgress()
            }
        }
    }

    private fun getData(isRefresh: Boolean = false) {
        detailViewModel.getData(
            intent.getStringExtra(ExtraKey.EXTRA_PACKAGE_NAME)!!,
            Utils.getAppName(
                this,
                intent.getStringExtra(ExtraKey.EXTRA_PACKAGE_NAME)!!
            ),
            intent.getStringExtra(ExtraKey.EXTRA_TITLE),
            isRefresh
        )
    }

    private fun showDeleteInfo(item: ContentRO) {
        CommonDialog(this)
            .setContent(getString(R.string.msg_delete_notification))
            .setPositive(getString(R.string.delete)) {
                detailViewModel.apply {
                    delete(
                        intent.getStringExtra(ExtraKey.EXTRA_PACKAGE_NAME)!!,
                        item.title,
                        item.pKey
                    ) { isSuccess ->
                        if (isSuccess)
                            getData(true)
                        else
                            Toast.makeText(this@DetailActivity, R.string.msg_delete_fail, Toast.LENGTH_SHORT).show()
                    }
                }
                it.dismiss()
            }
            .setNegative(getString(R.string.cancel)) {
                it.dismiss()
            }
            .show()
    }
}