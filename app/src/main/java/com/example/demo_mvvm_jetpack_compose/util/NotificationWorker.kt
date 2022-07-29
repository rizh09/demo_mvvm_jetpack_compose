package com.example.demo_mvvm_jetpack_compose.util

import android.annotation.SuppressLint
import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.demo_mvvm_jetpack_compose.data.model.Quote
import com.example.demo_mvvm_jetpack_compose.network.internet.QuoteService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val apiService: QuoteService
) : CoroutineWorker(appContext, workerParams) {

    //Do Dependency Injection inside CoroutineWorker, we need 3 steps.
    //1. To pass apiService to "NotificationWorker", we create CustomNotificationWorkerFactory
    //2. remove the default worker in manifests
    //3. setWorkerFactory in class that extends Application
    class CustomNotificationWorkerFactory @Inject constructor(private val apiService: QuoteService) :
        WorkerFactory() {
        override fun createWorker(
            appContext: Context,
            workerClassName: String,
            workerParameters: WorkerParameters
        ): ListenableWorker? {
            return NotificationWorker(appContext, workerParameters, apiService)
        }

    }

    @SuppressLint("RestrictedApi")
    override suspend fun doWork(): Result {

        return withContext(Dispatchers.IO) {
            val response = fetchOneRandomQuote()
            if (response.isSuccess) {
                NotificationHelper(context = applicationContext).createNotification(
                    //hashCode() - returns a numeric representation of an object’s contents
                    notificationId = response.quoteResult.hashCode(),
                    title = response.quoteResult.author,
                    message = response.quoteResult.content
                )
                Result.Success()
            } else {
                Result.Retry()
            }
            //example of getInputData from workerHelper
//            NotificationHelper(context = applicationContext).createNotification(
//                inputData.getString(WorkerHelper.TITLE).toString(),
//                inputData.getString(WorkerHelper.MESSAGE).toString()
//            )
        }
    }

    private suspend fun fetchOneRandomQuote(): Response {
        val rawResponse = apiService.getOneRandomQuote()

        return Response(
            isSuccess = rawResponse.isSuccessful,
            quoteResult = rawResponse.body()!!
        )
    }

    class Factory @Inject constructor(

    )
}

data class Response(
    val isSuccess: Boolean,
    val quoteResult: Quote.Result
)
