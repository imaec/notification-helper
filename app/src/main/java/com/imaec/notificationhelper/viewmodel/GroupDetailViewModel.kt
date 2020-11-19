package com.imaec.notificationhelper.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.imaec.notificationhelper.base.BaseViewModel
import com.imaec.notificationhelper.model.ContentRO
import com.imaec.notificationhelper.model.GroupDetailData
import com.imaec.notificationhelper.repository.NotificationRepository
import com.imaec.notificationhelper.ui.adapter.GroupDetailAdapter

@Suppress("UNCHECKED_CAST")
class GroupDetailViewModel(
    private val repository: NotificationRepository,
    private val packageName: String
) : BaseViewModel() {

    init {
        adapter = GroupDetailAdapter()
    }

    private val _listGroup = MutableLiveData<ArrayList<GroupDetailData>>(ArrayList())
    val listGroup: LiveData<ArrayList<GroupDetailData>> get() = _listGroup

    @RequiresApi(Build.VERSION_CODES.N)
    fun getData() {
        val listTemp = ArrayList<GroupDetailData>()
        (repository.getContents(packageName) as ArrayList<ContentRO>).groupBy { content ->
            content.title
        }.forEach { key, list ->
            listTemp.add(GroupDetailData(
                list[0].pKey,
                list[0].title,
                list[0].img,
                list[0].content,
                list.size,
                packageName
            ))
        }

        _listGroup.value = listTemp
    }
}