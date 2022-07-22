package com.example.demo_mvvm_jetpack_compose.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.demo_mvvm_jetpack_compose.data.database.DatabaseQuoteResult
import com.example.demo_mvvm_jetpack_compose.data.database.QuoteResultRemoteKey
import com.example.demo_mvvm_jetpack_compose.data.database.asDomainModel
import com.example.demo_mvvm_jetpack_compose.di.module.QuoteResultDatabase
import com.example.demo_mvvm_jetpack_compose.network.internet.QuoteService
import retrofit2.HttpException

@OptIn(ExperimentalPagingApi::class)
class QuoteRemoteMediator(
    private val db: QuoteResultDatabase,
    private val quoteName: String,
    private val quoteService: QuoteService,
) : RemoteMediator<Int, DatabaseQuoteResult>() {

    private val quoteResultRemoteKeyDao = db.quoteResultRemoteKeyDao()
    private val quoteResultDao = db.quoteResultDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, DatabaseQuoteResult>
    ): MediatorResult {
        try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    println("MediatorResult  APPEND start")
                    // Query DB for SubredditRemoteKey for the subreddit.
                    // SubredditRemoteKey is a wrapper object we use to keep track of page keys we
                    // receive from the Reddit API to fetch the next or previous page.
                    val remoteKey = db.withTransaction {
                        quoteResultRemoteKeyDao.getRemoteKeyByQuote(quoteName)
                    }

                    // We must explicitly check if the page key is null when appending, since the
                    // Reddit API informs the end of the list by returning null for page key, but
                    // passing a null key to Reddit API will fetch the initial page.
                    if (remoteKey.nextPageKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    println("MediatorResult  APPEND $remoteKey")

                    remoteKey.nextPageKey
                }
            }
            println("loadType $loadType  /  loadKey $loadKey  ")

            //loadkey's init state is null, we assign 1
            val data = quoteService.getQuotesByPage(loadKey ?: "1").body()!!

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    quoteResultDao.deleteAllQuote()
                    quoteResultRemoteKeyDao.deleteAllRemoteKey()
                }

                quoteResultRemoteKeyDao.insert(
                    QuoteResultRemoteKey(
                        quoteName,
                        (data.page + 1).toString()
                    )
                )
                quoteResultDao.insertAllByPaging(data.asDomainModel())
            }

            return MediatorResult.Success(endOfPaginationReached = data.results.isEmpty())
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }
}
