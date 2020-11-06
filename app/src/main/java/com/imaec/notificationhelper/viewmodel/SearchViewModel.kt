package com.imaec.notificationhelper.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.RequestManager
import com.imaec.notificationhelper.base.BaseViewModel
import com.imaec.notificationhelper.repository.NotificationRepository
import com.imaec.notificationhelper.ui.adapter.SearchAdapter

class SearchViewModel(
    private val repository: NotificationRepository,
    glide: RequestManager
) : BaseViewModel() {

    private val _listResult = MutableLiveData(ArrayList<Any>())
    val listResult: LiveData<ArrayList<Any>> get() = _listResult

    init {
        adapter = SearchAdapter(glide)
    }

    fun search(method: String, keyword: String) {
        _listResult.value = repository.search(method, keyword) as ArrayList<Any>
    }
}