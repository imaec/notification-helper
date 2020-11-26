package com.imaec.notificationhelper

const val ACTION_NOTIFICATION = "com.imaec.notificationhelper.broadcastreceiver.notification"
const val PREF_NAME = "NotificationHelper"

object ActivityRequestCode {
    const val REQUEST_SPLASH_FINISH = 101
}

object ItemType {
    const val TYPE_NAME = 1
    const val TYPE_CONTENT = 2
}

enum class PrefKey {
    PREF_NOT_SHOW_AGAIN
}