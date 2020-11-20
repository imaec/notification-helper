package com.imaec.notificationhelper.service

import android.annotation.SuppressLint
import android.app.Notification
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Icon
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.imaec.notificationhelper.ACTION_NOTIFICATION
import com.imaec.notificationhelper.model.ContentData
import com.imaec.notificationhelper.model.ContentRO
import com.imaec.notificationhelper.model.NotificationData
import com.imaec.notificationhelper.model.NotificationRO
import com.imaec.notificationhelper.utils.Utils
import io.realm.Realm

@SuppressLint("OverrideAbstract")
class NotificationHelperService : NotificationListenerService() {

    private val TAG = this::class.java.simpleName

    override fun onListenerConnected() {
        super.onListenerConnected()

        Log.d(TAG, "    ## service connected!")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)

        val bundle = sbn.notification.extras
        Log.d(TAG, "    ## bundle : $bundle")
        bundle.keySet().forEach {
            Log.d(TAG, "    ## keySet : $it / ${bundle.get(it)}")
        }

        val title = bundle.getString(Notification.EXTRA_TITLE) ?: return
        val content = bundle.getString(Notification.EXTRA_TEXT)
        val largeIconBig = bundle.get(Notification.EXTRA_LARGE_ICON_BIG) as Bitmap?
        val largeIcon = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
            Utils.getBitmap(this, bundle.get(Notification.EXTRA_LARGE_ICON) as Icon?)
        else null

        var bitmap2: Bitmap? = null

        sbn.notification.extras.getBundle("android.wearable.EXTENSIONS")?.let {
            for (key in it.keySet()) {
                it.get(key)?.let { value ->
                    when (key) {
                        "background" -> {
                            bitmap2 = value as Bitmap
                        }
                        else -> {}
                    }
                }
            }
        }

        val data = NotificationData().apply {
            this.packageName = sbn.packageName
            this.appName = Utils.getAppName(this@NotificationHelperService, sbn.packageName)
            this.saveTime = System.currentTimeMillis()
            this.content = ContentData().apply {
                this.pKey = System.currentTimeMillis()
                this.title = title
                this.content = content ?: ""
                this.img = Utils.getByteArray(largeIconBig ?: largeIcon)
                this.img2 = Utils.getByteArray(bitmap2)
            }
        }
        saveRealm(data)
    }

    private fun saveRealm(data: NotificationData) {
        val realm = Realm.getDefaultInstance()
        val realmResult = realm.where(NotificationRO::class.java)
            .equalTo("packageName", data.packageName)
            .findFirst()

        realm.executeTransaction {
            if (realmResult == null) {
                // Write NotificationRO, ContentRO
                realm.createObject(NotificationRO::class.java, data.packageName).apply {
                    appName = data.appName
                    saveTime = data.saveTime
                    contents.add(ContentRO().apply {
                        pKey = data.saveTime
                        title = data.content.title
                        content = data.content.content
                        img = data.content.img
                        img2 = data.content.img2
                    })
                    Log.d(TAG, "    ## write : notification, content")
                }
            } else {
                // Write ContentRO
                realmResult.saveTime = data.saveTime
                realmResult.contents.add(ContentRO().apply {
                    pKey = data.saveTime
                    title = data.content.title
                    content = data.content.content
                    img = data.content.img
                    img2 = data.content.img2
                })
                Log.d(TAG, "    ## write : content")
            }

            val intent = Intent(ACTION_NOTIFICATION)
            sendBroadcast(intent)
        }
    }
}