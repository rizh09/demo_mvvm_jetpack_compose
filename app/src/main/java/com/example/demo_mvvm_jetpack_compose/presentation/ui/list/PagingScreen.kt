package com.example.demo_mvvm_jetpack_compose.presentation.ui.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    PagingContent(
        data = quotes,
        onQuoteClick = onQuoteClick
    )
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

@Composable
fun CardViewRow(quoteResult: Quote.Result, onQuoteClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clickable { onQuoteClick(quoteResult.id) },
        elevation = 10.dp
    ) {
        Column(
            modifier = Modifier.padding(
                15.dp
            )
        ) {
            Text(buildAnnotatedString
            {
                withStyle(
                    style = SpanStyle(fontStyle = FontStyle.Italic)
                ) {
                    append(quoteResult.content)
                }
            })
            Text(buildAnnotatedString
            {
                withStyle(
                    style = SpanStyle(fontSize = 12.sp)
                )
                {
                    append(quoteResult.author)
                }
            })
        }
    }
}
