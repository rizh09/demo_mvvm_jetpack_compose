package com.example.demo_mvvm_jetpack_compose.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.demo_mvvm_jetpack_compose.util.Constant.Companion.ITEMS_PER_PAGE
import com.example.demo_mvvm_jetpack_compose.di.module.QuoteResultDatabase
import com.example.demo_mvvm_jetpack_compose.network.internet.QuoteService
import javax.inject.Inject

class QuoteResultPagingRepository
@Inject constructor(
    private val db: QuoteResultDatabase,
    private val quoteService: QuoteService,
) {
    @OptIn(ExperimentalPagingApi::class)
    fun dataOfQuoteResult() = Pager(
        config = PagingConfig(pageSize = ITEMS_PER_PAGE),
        remoteMediator = QuoteRemoteMediator(
            db = db,
            quoteName = "defaultQuoteRemoteKey",
            quoteService = quoteService
        )
    ) {
        db.quoteResultDao().getQuotResultPaging()
    }.flow
}
