package com.imaec.notificationhelper.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.imaec.notificationhelper.base.BaseViewModel
import com.imaec.notificationhelper.model.ContentRO
import com.imaec.notificationhelper.repository.NotificationRepository
import com.imaec.notificationhelper.ui.adapter.DetailAdapter

@Suppress("UNCHECKED_CAST")
class DetailViewModel(
    private val repository: NotificationRepository,
    private val packageName: String,
    private val onClickContent: (ContentRO, Boolean) -> Unit
) : BaseViewModel() {

    init {
        adapter = DetailAdapter { item, isImage ->
            onClickContent(item, isImage)
        }.apply {
            setPackageName(packageName)
        }
    }

    private val _listContent = MutableLiveData<ArrayList<Any>>(ArrayList())
    val listContent: LiveData<ArrayList<Any>> get() = _listContent

    fun getData() {
        _listContent.value?.let {
            if (it.size == 0) {
                _listContent.value = repository.getContents(packageName) as ArrayList<Any>
            }
        }
    }
}