package com.imaec.notificationhelper.repository

import android.content.Context
import android.widget.Toast
import com.imaec.notificationhelper.model.AppData
import com.imaec.notificationhelper.model.ContentRO
import com.imaec.notificationhelper.model.IgnoreRO
import com.imaec.notificationhelper.model.NotificationRO
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort

class NotificationRepository(
    private val context: Context
) {

    private val TAG = this::class.java.simpleName

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

    fun getContents(packageName: String, title: String? = null): List<ContentRO> {
        val listItem = ArrayList<ContentRO>()
        val realmResult = realm.where(NotificationRO::class.java)
            .equalTo("packageName", packageName)
            .findFirst()
        realmResult?.let {
            if (title == null) {
                listItem.addAll(it.contents)
            } else {
                it.contents.filter { content ->
                    content.title == title
                }.forEach { filteringContent ->
                    listItem.add(filteringContent)
                }
            }
        }
        return if (listItem.size == 1) listItem else listItem.reversed()
    }

    fun delete(packageName: String) {
        realm.executeTransaction {
            realm.where(NotificationRO::class.java)
                .equalTo("packageName", packageName)
                .findAll()
                .deleteAllFromRealm()
        }
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

    fun getIgnore(): RealmResults<IgnoreRO> {
        return realm.where(IgnoreRO::class.java).findAll()
    }

    fun setIgnore(item: AppData, isSelected: Boolean) {
        setIgnore(item.packageName, isSelected)
    }

    fun setIgnore(packageName: String, isSelected: Boolean = true) {
        realm.executeTransaction {
            val realmResult = realm.where(IgnoreRO::class.java)
                .equalTo("packageName", packageName)
                .findFirst()
            if (isSelected) {
                // 저장
                it.createObject(IgnoreRO::class.java).apply {
                    this.packageName = packageName
                }
            } else {
                // 삭제
                realmResult?.deleteFromRealm()
            }
        }
    }
}