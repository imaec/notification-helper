package com.imaec.notificationhelper.ui.view.fragment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.ui.adapter.SearchAdapter
import com.imaec.notificationhelper.base.BaseFragment
import com.imaec.notificationhelper.databinding.FragmentSearchBinding
import com.imaec.notificationhelper.model.ContentRO
import com.imaec.notificationhelper.model.NotificationRO
import io.realm.Realm
import io.realm.Sort
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    private val realm by lazy { Realm.getDefaultInstance() }
    private val adapter by lazy { SearchAdapter(Glide.with(this)) }
    private val layoutManager = LinearLayoutManager(context)

    private var method = "name"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        binding.apply {
            lifecycleOwner = this@SearchFragment
            recyclerSearch.adapter = adapter
            recyclerSearch.layoutManager = layoutManager
            recyclerSearch.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))

            radioGroupSearch.setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    radio_search_name.id -> {
                        method = "name"
                    }
                    radio_search_content.id -> {
                        method = "content"
                    }
                }
            }

            editSearch.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val keyword = edit_search.text.toString()
                    if (keyword == "") {
                        Toast.makeText(context, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show()
                        return@setOnEditorActionListener true
                    }
                    search(keyword)
                    return@setOnEditorActionListener true
                }
                false
            }
            imageSearch.setOnClickListener {
                val keyword = edit_search.text.toString()
                if (keyword == "") {
                    Toast.makeText(context, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                search(keyword)
            }
        }
    }

    private fun search(keyword: String) {
        hideKeyboard()
        val realmResult = if (method == "name") {
            realm.where(NotificationRO::class.java)
                .sort("appName", Sort.ASCENDING)
                .contains("appName", keyword)
                .findAll()
        } else {
            realm.where(ContentRO::class.java)
                .sort("pKey", Sort.DESCENDING)
                .contains("content", keyword)
                .findAll()
        }

        adapter.clearItem()
        if (realmResult.size == 0) {
            Toast.makeText(context, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
        } else {
            realmResult.forEach { content ->
                adapter.addItem(content)
            }
        }
        adapter.notifyDataSetChanged()
    }

    private fun hideKeyboard() {
        try {
            val imm = context!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            (context as Activity).currentFocus?.let {
                imm.hideSoftInputFromWindow(it.windowToken, 0)
            }
        } catch (e: Exception) {
            Log.d("exception :::: ", e.toString())
        }
    }
}