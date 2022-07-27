package com.example.demo_mvvm_jetpack_compose.presentation.ui.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.demo_mvvm_jetpack_compose.data.model.Quote
import com.example.demo_mvvm_jetpack_compose.presentation.viewmodel.QuoteResultViewModel
import com.example.demo_mvvm_jetpack_compose.util.LoadingContent
import com.example.demo_mvvm_jetpack_compose.util.collectAsStateWithLifecycle


@Composable
fun ListScreen(
    viewModel: QuoteResultViewModel,
    onQuoteClick: (String) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    QuoteResultsContent(
        loading = uiState.isLoading,
        quoteResult = uiState.quoteResults,
        onQuoteClick = onQuoteClick,
        onRefresh = viewModel::refresh
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuoteResultsContent(
    loading: Boolean,
    quoteResult: List<Quote.Result>,
    onQuoteClick: (String) -> Unit,
    onRefresh: () -> Unit,
) {
    LoadingContent(
        loading = loading,
        empty = quoteResult.isEmpty() && !loading,
        emptyContent = { QuoteResultEmptyContent() },
        onRefresh = onRefresh
    ) {
        //it skips the forEach
        LazyVerticalGrid(
            cells = GridCells.Adaptive(128.dp),
            content = {
                items(quoteResult) { it ->
                    CardViewRow(quoteResult = it, onQuoteClick = onQuoteClick)
                }
            })
    }
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

@Composable
fun SingleQuoteResultDetail(quoteID: String, viewModel: QuoteResultViewModel) {
    val quoteResult = viewModel.getData().observeAsState().value?.first { it.id == quoteID }
    if (quoteResult != null) {
        SingleQuoteResultContent(quoteResult = quoteResult)
    }
}

@Composable
fun SingleQuoteResultContent(quoteResult: Quote.Result) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(
            text = quoteResult.content
        )
        Text(
            modifier = Modifier.padding(20.dp),
            text = quoteResult.author
        )
    }
}


@Composable
private fun QuoteResultEmptyContent(
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("empty")
    }
}
