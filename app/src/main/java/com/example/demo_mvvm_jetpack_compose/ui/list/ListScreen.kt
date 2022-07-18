package com.example.demo_mvvm_jetpack_compose.ui.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.demo_mvvm_jetpack_compose.model.Quote
import com.example.demo_mvvm_jetpack_compose.viewmodel.QuoteResultViewModel
import javax.inject.Inject


@Composable
fun ListBody (
    quoteResult: List<Quote.Result>,
    onQuoteClick: (String) -> Unit = {},
) {
    MessageGridListWithLazy(quoteResult = quoteResult, onQuoteClick = onQuoteClick)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageGridListWithLazy(quoteResult: List<Quote.Result>, onQuoteClick: (String) -> Unit) {
    //it skips the forEach
    LazyVerticalGrid(
        cells = GridCells.Adaptive(128.dp),
        content = {
            items(quoteResult) { it ->
                //MessageRow(number = index.author)
                CardViewRow(quoteResult = it, onQuoteClick = onQuoteClick)
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

@Composable
fun SingleQuoteResultDetail(quoteResult: Quote.Result) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = quoteResult.id)
        Text(text = quoteResult.author)
        Text(text = quoteResult.content)
    }
}
