package com.example.demo_mvvm_jetpack_compose.presentation

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import com.example.demo_mvvm_jetpack_compose.util.NotificationWorker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

//All apps that use Hilt must contain an Application class that is annotated with @HiltAndroidApp.
//These annotations tell Hilt to trigger the code generation that Dagger will pick up and use in its annotation processor.

//ref : https://developer.android.com/training/dependency-injection/hilt-android
@HiltAndroidApp
class MainApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: NotificationWorker.CustomNotificationWorkerFactory
    //test the auto build on appcenter
    override fun getWorkManagerConfiguration() =
        Configuration
            .Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(Log.VERBOSE).build()
}
