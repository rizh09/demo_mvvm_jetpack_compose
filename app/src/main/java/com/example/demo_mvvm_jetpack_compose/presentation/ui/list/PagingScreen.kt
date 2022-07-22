package com.example.demo_mvvm_jetpack_compose.presentation.ui.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.demo_mvvm_jetpack_compose.data.model.Quote
import com.example.demo_mvvm_jetpack_compose.presentation.viewmodel.QuoteResultViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagingScreen(
    viewModel: QuoteResultViewModel,
    onQuoteClick: (String) -> Unit = {}
) {
    val quotes = viewModel.pagingData.collectAsLazyPagingItems()
    PagingContent(data = quotes, onQuoteClick = onQuoteClick)
}

@Composable
fun PagingContent(
    data: LazyPagingItems<Quote.Result>,
    onQuoteClick: (String) -> Unit = {}
) {
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
