package com.imaec.notificationhelper.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.RequestManager
import com.imaec.notificationhelper.base.BaseViewModel
import com.imaec.notificationhelper.model.AppData
import com.imaec.notificationhelper.model.IgnoreRO
import com.imaec.notificationhelper.repository.NotificationRepository
import com.imaec.notificationhelper.ui.adapter.AppAdapter
import com.imaec.notificationhelper.ui.view.fragment.SettingFragment
import io.realm.RealmResults

@Suppress("UNCHECKED_CAST")
class SettingViewModel(
    private val repository: NotificationRepository
) : BaseViewModel() {

    init {
        adapter = AppAdapter { item, isSelected ->
            repository.setIgnore(item.packageName, isSelected)
        }
    }

    private val _listApp = MutableLiveData(ArrayList<Any>())
    val listApp: LiveData<ArrayList<Any>> get() = _listApp

    fun setListApp(list: ArrayList<AppData>) {
        _listApp.value = list as ArrayList<Any>
    }

    fun getIgnore() {
        listApp.value?.let {
            if (it.size > 0) {
                (adapter as AppAdapter).setSelectedItems(repository.getIgnore())
                adapter.notifyDataSetChanged()
            }
        }
    }
}