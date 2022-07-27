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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.demo_mvvm_jetpack_compose.data.model.Tags
import com.example.demo_mvvm_jetpack_compose.presentation.viewmodel.QuoteTagViewModel
import com.example.demo_mvvm_jetpack_compose.util.LoadingContent
import com.example.demo_mvvm_jetpack_compose.util.collectAsStateWithLifecycle


@Composable
fun ListScreen(
    viewModel: QuoteTagViewModel,
    onQuoteClick: (String) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    QuoteTagsContent(
        loading = uiState.isLoading,
        quoteResult = uiState.quoteTags,
        onQuoteClick = onQuoteClick,
        onRefresh = viewModel::refresh
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuoteTagsContent(
    loading: Boolean,
    quoteResult: List<Tags.TagsItem>,
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
fun CardViewRow(quoteResult: Tags.TagsItem, onQuoteClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clickable { onQuoteClick(quoteResult.name) },
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
                    append(quoteResult.name)
                }
            })
            Text(buildAnnotatedString
            {
                withStyle(
                    style = SpanStyle(fontSize = 12.sp)
                )
                {
                    append(quoteResult.quoteCount)
                }
            })
        }
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
