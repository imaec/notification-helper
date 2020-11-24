package com.imaec.notificationhelper.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel

abstract class BaseViewModel : ViewModel() {

    protected val TAG = this::class.java.simpleName

    lateinit var adapter: BaseAdapter

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }

    open fun addOnClickListener(onClick: (Any) -> Unit) {
        adapter.addOnClickListener(onClick)
    }

    open fun addOnLongClickListener(onLongClick: (Any) -> Unit) {
        adapter.addOnLongClickListener(onLongClick)
    }
}