package com.imaec.notificationhelper.model

import java.io.Serializable

class ContentData : Serializable {

    var pKey: Long = 0 // Notification 받은 시간
    var title: String = ""
    var content: String = ""
    var img: ByteArray? = null
    var img2: ByteArray? = null
}