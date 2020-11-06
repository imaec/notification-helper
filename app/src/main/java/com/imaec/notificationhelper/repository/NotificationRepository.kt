package com.imaec.notificationhelper.repository

import android.content.Context
import android.widget.Toast
import com.imaec.notificationhelper.model.ContentRO
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

    fun search(method: String, keyword: String): List<Any> {
        val realmResult = if (method == "name") {
            realm.where(NotificationRO::class.java)
                .sort("appName", Sort.ASCENDING)
                .contains("appName", keyword)
                .findAll()
        } else {
            realm.where(ContentRO::class.java)
                .sort("pKey", Sort.DESCENDING)
                .contains("content", keyword)
                .findAll()
        }

        val listTemp = ArrayList<Any>()
        if (realmResult.size == 0) {
            Toast.makeText(context, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
        } else {
            realmResult.forEach { content ->
                listTemp.add(content)
            }
        }
        return listTemp
    }
}