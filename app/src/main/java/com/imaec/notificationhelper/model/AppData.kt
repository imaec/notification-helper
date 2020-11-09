package com.imaec.notificationhelper.model

import android.graphics.drawable.Drawable

data class AppData(
    var icon: Drawable,
    var packageName: String = "",
    var name: String = ""
)