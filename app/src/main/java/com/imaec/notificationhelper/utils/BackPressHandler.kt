package com.imaec.notificationhelper.utils

import android.widget.Toast
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by imaec on 2018-06-27.
 */
class BackPressHandler(private val activity: AppCompatActivity) {

    private var backKeyPressedTime: Long = 0
    private var toast: Toast? = null

    fun onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis()
            showGuide()
            return
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finish()
            toast!!.cancel()
        }
    }

    private fun showGuide() {
        toast = Toast.makeText(activity, "\'뒤로\' 버튼을 한번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT)
        toast!!.show()
    }
}