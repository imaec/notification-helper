package com.imaec.notificationhelper.service

import android.annotation.SuppressLint
import android.app.Notification
import android.content.Intent
import android.graphics.Bitmap
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.imaec.notificationhelper.ACTION_NOTIFICATION
import com.imaec.notificationhelper.Utils
import com.imaec.notificationhelper.model.ContentData
import com.imaec.notificationhelper.model.ContentRO
import com.imaec.notificationhelper.model.NotificationData
import com.imaec.notificationhelper.model.NotificationRO
import io.realm.Realm
import java.io.ByteArrayOutputStream

@SuppressLint("OverrideAbstract")
class NotificationHelperService : NotificationListenerService() {

    override fun onListenerConnected() {
        super.onListenerConnected()

        Log.d("service :::: ", "connected!")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)

        val bundle = sbn.notification.extras
        Log.d("bundle :::: ", bundle.toString())

        val title = bundle.getString(Notification.EXTRA_TITLE) ?: return
        val content = bundle.getString(Notification.EXTRA_TEXT)
        val bitmap = bundle.get(Notification.EXTRA_LARGE_ICON) as Bitmap?
        var bitmap2: Bitmap? = null

        sbn.notification.extras.getBundle("android.wearable.EXTENSIONS")?.let {
            for (key in it.keySet()) {
                it.get(key)?.let { obj ->
//                    Log.d("key :::: ", key)
//                    Log.d("obj :::: ", obj.toString())
                    if (key.toString() == "background") {
                        bitmap2 = obj as Bitmap
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
                this.img = Utils.getByteArray(bitmap)
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
                    Log.d("write :::: ", "notification, content")
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
                Log.d("write :::: ", "content")
            }

            val intent = Intent(ACTION_NOTIFICATION)
            sendBroadcast(intent)
        }
    }
}