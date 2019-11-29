package com.imaec.notificationhelper.model

import io.realm.RealmObject

open class IgnoreRO : RealmObject() {

    var ignoreIndex: Int = -1
    var packageName: String = ""
}