package com.example.demo_mvvm_jetpack_compose.presentation.ui.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.demo_mvvm_jetpack_compose.data.model.Quote
import com.example.demo_mvvm_jetpack_compose.presentation.viewmodel.QuoteResultViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagingWithSearchScreen(
    viewModel: QuoteResultViewModel,
    onQuoteClick: (String) -> Unit = {},
    onSearchClicked: (String) -> Unit = {}
) {

    val searchQuery by viewModel.searchQuery

    val searchQuotes = viewModel.searchedQuotes.collectAsLazyPagingItems()
    PagingWithSearchContent(
        updateSearchQuery = { viewModel.updateSearchQuery(it) },
        searchQuery = searchQuery,
        onSearchClicked = onSearchClicked,
        data = searchQuotes,
        onQuoteClick = onQuoteClick
    )
}

@Composable
fun PagingWithSearchContent(
    searchQuery: String,
    data: LazyPagingItems<Quote.Result>,
    onQuoteClick: (String) -> Unit = {},
    onSearchClicked: (String) -> Unit = {},
    updateSearchQuery: (String) -> Unit = {}
) {

    // Calling the function FilteredTextField
    Scaffold(
        topBar = {
            SearchWidget(
                text = searchQuery,
                onTextChange = {
                    updateSearchQuery(it)
                },
                onSearchClicked = {
                    onSearchClicked(it)
                }
            )
        },
        content = {
            LazyColumn(content = {
                items(data.itemCount) { index ->
                    data[index]?.let {
                        CardViewRow(
                            it,
                            onQuoteClick
                        )
                    }
                }
            })
        }
    )
}
