package com.imaec.notificationhelper.model

import java.io.Serializable

class NotificationData: Serializable {

    var packageName: String = ""
    var appName: String = ""
    var saveTime: Long = 0
    lateinit var content: ContentData
}