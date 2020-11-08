package com.imaec.notificationhelper

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.imaec.notificationhelper.base.BaseViewModelFactory

object Extensions {

    inline fun <reified V : ViewModel> AppCompatActivity.getViewModel(noinline creator: (() -> V)?) : V {
        creator?.let {
            return ViewModelProvider(this,
                BaseViewModelFactory(creator)
            ).get(V::class.java)
        }
        return ViewModelProviders.of(this).get(V::class.java)
    }

    inline fun <reified V : ViewModel> Fragment.getViewModel(noinline creator: (() -> V)?) : V {
        creator?.let {
            return ViewModelProvider(this,
                BaseViewModelFactory(creator)
            ).get(V::class.java)
        }
        return ViewModelProviders.of(this).get(V::class.java)
    }
}