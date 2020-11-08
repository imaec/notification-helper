package com.imaec.notificationhelper.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.imaec.notificationhelper.base.BaseViewModel
import com.imaec.notificationhelper.model.AppData
import com.imaec.notificationhelper.repository.NotificationRepository

@Suppress("UNCHECKED_CAST")
class SettingViewModel(
    private val repository: NotificationRepository
) : BaseViewModel() {

    private val _listApp = MutableLiveData(ArrayList<Any>())
    val listApp: LiveData<ArrayList<Any>> get() = _listApp

    fun setListApp(list: ArrayList<AppData>) {
        _listApp.value = list as ArrayList<Any>
    }

    fun ignoreFiltering() {

    }

    fun setIgnore(position: Int, isSelected: Boolean) {
        repository.setIgnore(_listApp.value as List<AppData>, position, isSelected)
    }
}