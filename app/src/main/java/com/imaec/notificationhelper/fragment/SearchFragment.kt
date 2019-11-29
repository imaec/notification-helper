package com.imaec.notificationhelper.fragment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.imaec.notificationhelper.R
import com.imaec.notificationhelper.adapter.SearchAdapter
import com.imaec.notificationhelper.model.ContentRO
import com.imaec.notificationhelper.model.NotificationRO
import io.realm.Realm
import io.realm.Sort
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {

    private lateinit var realm: Realm
    private lateinit var adapter: SearchAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private var method = "name"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        radioGroupSearch.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                radioSearchName.id -> {
                    method = "name"
                }
                radioSearchContent.id -> {
                    method = "content"
                }
            }
        }

        editSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val keyword = editSearch.text.toString()
                if (keyword == "") {
                    Toast.makeText(context, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    return@setOnEditorActionListener true
                }
                search(keyword)
                true
            }
            false
        }
        imageSearch.setOnClickListener {
            val keyword = editSearch.text.toString()
            if (keyword == "") {
                Toast.makeText(context, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            search(keyword)
        }
    }

    private fun init() {
        realm = Realm.getDefaultInstance()
        adapter = SearchAdapter(Glide.with(this))
        layoutManager = LinearLayoutManager(context)

        recyclerSearch.adapter = adapter
        recyclerSearch.layoutManager = layoutManager
        recyclerSearch.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))
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

        if (realmResult.size == 0) {
            Toast.makeText(context, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        adapter.clearItem()
        realmResult.forEach { content ->
            adapter.addItem(content)
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