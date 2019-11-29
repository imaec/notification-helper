package com.imaec.notificationhelper.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

open class ContentRO : RealmObject(), Serializable {

    @PrimaryKey
    var pKey: Long = 0 // Notification 받은 시간
    var title: String = ""
    var content: String = ""
    var img: ByteArray? = null
    var img2: ByteArray? = null
}