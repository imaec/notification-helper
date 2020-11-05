package com.imaec.notificationhelper.ui.view.activity

import android.os.Bundle
import com.bumptech.glide.Glide
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.utils.Utils
import com.imaec.notificationhelper.base.BaseActivity
import com.imaec.notificationhelper.databinding.ActivityImageBinding

class ImageActivity : BaseActivity<ActivityImageBinding>(R.layout.activity_image) {

    private lateinit var img: ByteArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
    }

    private fun init() {
        img = intent.getByteArrayExtra("img")
        binding.apply {
            lifecycleOwner = this@ImageActivity
        }
        Glide.with(this)
            .load(Utils.getBitmap(img))
            .into(binding.imageDetail)
    }
}