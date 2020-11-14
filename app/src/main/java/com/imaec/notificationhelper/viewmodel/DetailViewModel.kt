package com.imaec.notificationhelper.viewmodel

import com.imaec.notificationhelper.base.BaseViewModel
import com.imaec.notificationhelper.model.ContentRO
import com.imaec.notificationhelper.ui.adapter.DetailAdapter

class DetailViewModel(
    private val onClickContent: (ContentRO, Boolean) -> Unit
) : BaseViewModel() {

    init {
        adapter = DetailAdapter { item, isImage ->
            onClickContent(item, isImage)
        }
    }

    fun getData() {

    }
}