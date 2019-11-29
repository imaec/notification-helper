package com.imaec.notificationhelper.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.Utils
import kotlinx.android.synthetic.main.activity_image.*

class ImageActivity : AppCompatActivity() {

    private lateinit var img: ByteArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        init()
    }

    private fun init() {
        img = intent.getByteArrayExtra("img")
        Glide.with(this)
            .load(Utils.getBitmap(img))
            .into(imageDetail)
    }
}