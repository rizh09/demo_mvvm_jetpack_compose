package com.example.demo_mvvm_jetpack_compose.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.demo_mvvm_jetpack_compose.di.module.QuoteResultDatabase
import com.example.demo_mvvm_jetpack_compose.network.internet.QuoteService
import com.example.demo_mvvm_jetpack_compose.util.Constant.Companion.ITEMS_PER_PAGE
import javax.inject.Inject

class QuoteResultPagingRepository
@Inject constructor(
    private val db: QuoteResultDatabase,
    private val quoteService: QuoteService,
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getDataOfQuoteResult() = Pager(
        config = PagingConfig(pageSize = ITEMS_PER_PAGE),
        remoteMediator = QuoteRemoteMediator(
            db = db,
            keywords = "random",
            quoteService = quoteService
        )
    ) {
        db.quoteResultDao().getQuotResultPaging()
    }.flow

    @OptIn(ExperimentalPagingApi::class)
    fun getDataOfQuoteResultByKeywords(keywords: String? = null) = Pager(
        config = PagingConfig(pageSize = ITEMS_PER_PAGE),
        remoteMediator = QuoteRemoteMediator(
            db = db,
            keywords = keywords ?: "",
            quoteService = quoteService
        )
    ) {
        db.quoteResultDao().getQuotResultPagingByKeywords(keywords = keywords ?: "")
    }.flow
}
