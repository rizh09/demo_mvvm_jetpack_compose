package com.example.demo_mvvm_jetpack_compose.util

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

class WorkerHelper(private val context: Context) {

    //Two types of work request
    //PeriodicWorkRequest
    //OneTimeWorkRequest
    fun createWorkRequest(repeatIntervalInHours: Long, timeDelayInMinutes: Long) {

        //example of OneTimeWorkRequest
//        val myWorkRequest = OneTimeWorkRequestBuilder<WorkerManager>()
//            .setInitialDelay(timeDelayInSeconds, TimeUnit.SECONDS)
//            .setInputData(
//                workDataOf(
//                    TITLE to "Reminder",
//                    MESSAGE to message,
//                )
//            )
//            .build()
//        WorkManager.getInstance(context).enqueue(myWorkRequest)

        val constants = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(false)
            .build()

        //example of PeriodicWorkRequest
        val myWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            repeatInterval = repeatIntervalInHours,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        )
            .setInitialDelay(
                timeDelayInMinutes, TimeUnit.MINUTES
            )
            .setConstraints(constants)
//            .setInputData(
//                workDataOf(
//                    TITLE to "Reminder",
//                    MESSAGE to message,
//                )
//            )
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

        WorkManager.getInstance(context).enqueue(myWorkRequest)
    }

//    companion object {
//        const val TITLE = "title"
//        const val MESSAGE = "message"
//    }
}
