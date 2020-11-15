package com.imaec.notificationhelper.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.imaec.notificationhelper.base.BaseViewModel
import com.imaec.notificationhelper.repository.NotificationRepository
import com.imaec.notificationhelper.ui.adapter.NotificationAdapter

@Suppress("UNCHECKED_CAST")
class NotificationViewModel(
    private val repository: NotificationRepository
) : BaseViewModel() {

    init {
        adapter = NotificationAdapter()
    }

    private val _listNotification = MutableLiveData<ArrayList<Any>>(ArrayList())
    val listNotification: LiveData<ArrayList<Any>> get() = _listNotification

    fun getNotifications() {
        _listNotification.value = repository.getNotifications() as ArrayList<Any>
    }
}