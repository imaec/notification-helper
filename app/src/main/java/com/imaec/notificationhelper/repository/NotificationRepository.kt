package com.imaec.notificationhelper.repository

import android.content.Context
import com.imaec.notificationhelper.model.IgnoreRO
import com.imaec.notificationhelper.model.NotificationRO
import io.realm.Realm
import io.realm.Sort

class NotificationRepository(
    private val context: Context
) {

    private val realm by lazy { Realm.getDefaultInstance() }

    fun getNotifications(): List<NotificationRO> {
        val realmResult = realm.where(NotificationRO::class.java)
            .sort("saveTime", Sort.DESCENDING)
            .findAll()

        val listTemp = ArrayList<NotificationRO>()
        realmResult.forEach {
            val ignore = realm.where(IgnoreRO::class.java)
                .equalTo("packageName", it.packageName)
                .findFirst()
            if (ignore == null) {
                listTemp.add(it)
            }
        }
        return listTemp
    }
}