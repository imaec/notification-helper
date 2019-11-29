package com.imaec.notificationhelper.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class NotificationRO : RealmObject() {

    @PrimaryKey
    var packageName: String = ""
    var appName: String = ""
    var saveTime: Long = 0 // 마지막으로 Notification 받은 시간
    lateinit var contents: RealmList<ContentRO>
}