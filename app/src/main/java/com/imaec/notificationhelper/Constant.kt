package com.imaec.notificationhelper

const val ACTION_NOTIFICATION = "com.imaec.notificationhelper.broadcastreceiver.notification"
const val PREF_NAME = "NotificationHelper"

object ActivityRequestCode {
    const val REQUEST_SPLASH_FINISH = 101
    const val REQUEST_DETAIL = 201
    const val REQUEST_GROUP_DETAIL = 202
}

object ItemType {
    const val TYPE_NAME = 1
    const val TYPE_CONTENT = 2
}

object ExtraKey {
    const val EXTRA_TITLE = "extra_title"
    const val EXTRA_PACKAGE_NAME = "extra_package_name"
}

enum class PrefKey {
    PREF_NOT_SHOW_AGAIN
}