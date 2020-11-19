package com.imaec.notificationhelper.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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

                val a = repository.getContents(packageName) as ArrayList<ContentRO>
                val group2 = a.groupBy { content ->
                    content.title
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    group2.forEach { s, list ->
                        Log.d(TAG, "    ## key : $s")
                        Log.d(TAG, "    ## list : $list")
                    }
                }
            }
        }
    }
}