package com.imaec.notificationhelper.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.imaec.notificationhelper.base.BaseViewModel
import com.imaec.notificationhelper.model.ContentRO
import com.imaec.notificationhelper.repository.NotificationRepository
import com.imaec.notificationhelper.ui.adapter.DetailAdapter

@Suppress("UNCHECKED_CAST")
class DetailViewModel(
    private val repository: NotificationRepository,
    private val onClickContent: (ContentRO, Boolean) -> Unit
) : BaseViewModel() {

    private var packageName = ""

    init {
        adapter = DetailAdapter { item, isImage ->
            onClickContent(item, isImage)
        }.apply {
            setPackageName(packageName)
        }
    }

    private val _listContent = MutableLiveData<ArrayList<ContentRO>>(ArrayList())
    val listContent: LiveData<ArrayList<ContentRO>> get() = _listContent

    fun getData(packageName: String, title: String? = null) {
        this.packageName = packageName
        (adapter as DetailAdapter).setPackageName(packageName)
        _listContent.value?.let {
            if (it.size == 0) {
                _listContent.value = repository.getContents(packageName, title) as ArrayList<ContentRO>
            }
        }
    }
}