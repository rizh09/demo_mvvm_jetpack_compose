package com.example.demo_mvvm_jetpack_compose.data.repository

import com.example.demo_mvvm_jetpack_compose.data.database.asDomainModel
import com.example.demo_mvvm_jetpack_compose.data.model.Tags
import com.example.demo_mvvm_jetpack_compose.di.module.QuoteTagDao
import com.example.demo_mvvm_jetpack_compose.network.internet.QuoteService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QuoteTagRepository @Inject constructor(
    private val quoteTagDao: QuoteTagDao,
    private val quoteService: QuoteService,
    private val defaultDispatcher: CoroutineDispatcher
) : RepositoryInterface {

    //ref: https://github.com/google-developer-training/android-kotlin-fundamentals-apps/blob/master/RepositoryPattern/app/src/main/java/com/example/android/devbyteviewer/repository/VideosRepository.kt

    //we can do Intermediate operation in Repository e.g filter
    fun getQuoteTags(): Flow<List<Tags.TagsItem>> =
        quoteTagDao.getQuoteTags().map { it.asDomainModel() }.flowOn(defaultDispatcher)

    override suspend fun refreshLocalDataByAPICall() {
        withContext(Dispatchers.IO) {
            //api call
            val body = quoteService.getTags().body()

            body.let {
                if (it != null) {
                    quoteTagDao.insertAll(it.asDomainModel())
                }
            }
        }
    }
}
