package com.imaec.notificationhelper.ui.view.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.imaec.notificationhelper.Extensions.getViewModel
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.base.BaseFragment
import com.imaec.notificationhelper.databinding.FragmentSearchBinding
import com.imaec.notificationhelper.repository.NotificationRepository
import com.imaec.notificationhelper.ui.view.activity.DetailActivity
import com.imaec.notificationhelper.ui.view.activity.GroupDetailActivity
import com.imaec.notificationhelper.ui.view.activity.ImageActivity
import com.imaec.notificationhelper.utils.KeyboardUtil
import com.imaec.notificationhelper.viewmodel.SearchViewModel

class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    private lateinit var searchViewModel: SearchViewModel

    private var method = "name"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        searchViewModel = getViewModel {
            SearchViewModel(NotificationRepository(context!!), {
                startActivity(Intent(context, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    GroupDetailActivity::class.java
                } else {
                    DetailActivity::class.java
                }).apply {
                    putExtra("packageName", it.packageName)
                })
            }, { item, isImage ->
                if (isImage) {
                    startActivity(Intent(context, ImageActivity::class.java).apply {
                        putExtra("img", item.img2)
                    })
                } else {
                    AlertDialog.Builder(binding.root.context).apply {
                        setTitle(item.title)
                        setMessage(item.content)
                        setPositiveButton("확인") { dialog, which ->
                            dialog.dismiss()
                        }
                        create()
                        show()
                    }
                }
            })
        }

        binding.apply {
            lifecycleOwner = this@SearchFragment
            searchViewModel = this@SearchFragment.searchViewModel

            recyclerSearch.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            radioGroupSearch.setOnCheckedChangeListener { group, checkedId ->
                method = if (checkedId == R.id.radio_search_name) "name" else "content"
            }
            editSearch.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search(binding.editSearch.text.toString())
                    return@setOnEditorActionListener true
                }
                false
            }
            imageSearch.setOnClickListener {
                search(binding.editSearch.text.toString())
            }
        }
    }

    private fun search(keyword: String) {
        if (keyword.isEmpty()) {
            Toast.makeText(context, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show()
        } else {
            KeyboardUtil.hideKeyboardFrom(context!!)
            this@SearchFragment.searchViewModel.search(method, keyword)
        }
    }
}