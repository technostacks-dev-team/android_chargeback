package com.technostacks.chargebackui.mvvm.extensions

import androidx.appcompat.widget.SearchView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

fun SearchView.getQueryTextSubmittedListener(onQueryTextSubmitted: (String?) -> Unit): SearchView.OnQueryTextListener {
    val listener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            onQueryTextSubmitted(query)
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            onQueryTextSubmitted(newText)
            return true
        }
    }
    setOnQueryTextListener(listener)
    return listener
}

