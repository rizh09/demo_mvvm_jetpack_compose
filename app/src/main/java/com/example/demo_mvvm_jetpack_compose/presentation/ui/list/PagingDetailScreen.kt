package com.example.demo_mvvm_jetpack_compose.presentation.ui.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.demo_mvvm_jetpack_compose.data.model.Quote
import com.example.demo_mvvm_jetpack_compose.presentation.viewmodel.QuoteResultViewModel

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
