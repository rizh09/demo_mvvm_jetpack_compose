package com.example.demo_mvvm_jetpack_compose.util

import android.content.Context
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import dagger.hilt.android.internal.Contexts

fun initAppCenter(applicationContext: Context){
    AppCenter.start(
        Contexts.getApplication(applicationContext), "b6b33d03-2de6-40f6-8606-7b4ffc38aadd",
        Analytics::class.java, Crashes::class.java
    )
}
