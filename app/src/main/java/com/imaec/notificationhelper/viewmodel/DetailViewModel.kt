package com.imaec.notificationhelper.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.imaec.notificationhelper.base.BaseViewModel
import com.imaec.notificationhelper.model.ContentRO
import com.imaec.notificationhelper.repository.NotificationRepository
import com.imaec.notificationhelper.ui.adapter.DetailAdapter
import com.imaec.notificationhelper.utils.Utils

@Suppress("UNCHECKED_CAST")
class DetailViewModel(
    private val repository: NotificationRepository,
    private val onClickContent: (ContentRO, Boolean) -> Unit
) : BaseViewModel() {

    private val _packageName = MutableLiveData("")
    val packageName: LiveData<String> get() = _packageName

    init {
        adapter = DetailAdapter { item, isImage ->
            onClickContent(item, isImage)
        }.apply {
            setPackageName(packageName.value!!)
        }
    }

    private val _title = MutableLiveData("")
    val title: LiveData<String> get() = _title

    private val _icon = MutableLiveData(byteArrayOf())
    val icon: LiveData<ByteArray> get() = _icon

    private val _listContent = MutableLiveData<ArrayList<ContentRO>>(ArrayList())
    val listContent: LiveData<ArrayList<ContentRO>> get() = _listContent

    fun getData(packageName: String, appName: String, title: String? = null) {
        _packageName.value = packageName
        _title.value = title ?: appName
        (adapter as DetailAdapter).setPackageName(packageName)
        _listContent.value?.let {
            if (it.size == 0) {
                _listContent.value = repository.getContents(packageName, title) as ArrayList<ContentRO>
                _icon.value = listContent.value?.get(0)?.img
            }
        }
    }
}