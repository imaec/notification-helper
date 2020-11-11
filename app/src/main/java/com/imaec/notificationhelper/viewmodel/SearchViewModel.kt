package com.imaec.notificationhelper.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.imaec.notificationhelper.base.BaseViewModel
import com.imaec.notificationhelper.model.ContentRO
import com.imaec.notificationhelper.model.NotificationRO
import com.imaec.notificationhelper.repository.NotificationRepository
import com.imaec.notificationhelper.ui.adapter.SearchAdapter

class SearchViewModel(
    private val repository: NotificationRepository,
    private val onClickNotification: (NotificationRO) -> Unit,
    private val onClickContent: (ContentRO, Boolean) -> Unit
) : BaseViewModel() {

    private val _listResult = MutableLiveData(ArrayList<Any>())
    val listResult: LiveData<ArrayList<Any>> get() = _listResult

    init {
        adapter = SearchAdapter({
            onClickNotification(it)
        }, { item, isImage ->
            onClickContent(item, isImage)
        })
    }

    fun search(method: String, keyword: String) {
        _listResult.value = repository.search(method, keyword) as ArrayList<Any>
    }
}