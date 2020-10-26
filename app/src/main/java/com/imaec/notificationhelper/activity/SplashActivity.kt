package com.imaec.notificationhelper.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.base.BaseActivity
import com.imaec.notificationhelper.databinding.ActivitySplashBinding
import com.imaec.notificationhelper.ui.view.activity.MainActivity

@Suppress("REDUNDANT_LABEL_WARNING")
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@SplashActivity
        }

        MainActivity.adLoaded.observe(this) {
            if (it) {
                setResult(RESULT_OK)
                finish()
            }
        }
    }
}