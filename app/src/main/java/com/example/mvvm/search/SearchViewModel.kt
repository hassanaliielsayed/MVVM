package com.example.mvvm.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel: ViewModel() {

    private val names = listOf(
        "Hassan",
        "Kiro",
        "Ziad",
        "Ewida",
        "Yousef",
        "Rana",
        "Basel",
        "Mostafa",
        "Mo"
    )

    private val _query = MutableStateFlow("")

    private val _filteredNames = MutableStateFlow(names)
    val filteredNames: StateFlow<List<String>> = _filteredNames.asStateFlow()

//    init {
//        // Observe changes to the search query and update the filtered list
//        viewModelScope.launch {
//            _query.collect { query ->
//                filterNames(query)
//            }
//        }
//    }

    fun setSearchQuery(query: String) {
        _query.value = query
        filterNames(query)
    }

    private fun filterNames(query: String) {
        viewModelScope.launch {
            _filteredNames.update {
                if (query.isEmpty()) {
                    names
                } else {
                    names.filter { it.contains(query, ignoreCase = true) } // Filter names
                }
            }
        }
    }
}