package com.imaec.notificationhelper.ui.view.fragment

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.base.BaseFragment
import com.imaec.notificationhelper.databinding.FragmentSearchBinding
import com.imaec.notificationhelper.repository.NotificationRepository
import com.imaec.notificationhelper.utils.KeyboardUtil
import com.imaec.notificationhelper.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    private lateinit var searchViewModel: SearchViewModel

    private var method = "name"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        searchViewModel = SearchViewModel(NotificationRepository(context!!), Glide.with(this))

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