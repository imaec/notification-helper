package com.imaec.notificationhelper.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.ui.view.activity.MainActivity

@Suppress("REDUNDANT_LABEL_WARNING")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        MainActivity.adLoaded.observe(this, Observer {
            Log.d(this::class.java.simpleName, "$it")

            if (it) {
                setResult(RESULT_OK)
                finish()
            }
        })
    }
}