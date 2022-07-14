package com.example.demo_mvvm_jetpack_compose

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

//All apps that use Hilt must contain an Application class that is annotated with @HiltAndroidApp.
//These annotations tell Hilt to trigger the code generation that Dagger will pick up and use in its annotation processor.

//ref : https://developer.android.com/training/dependency-injection/hilt-android
@HiltAndroidApp
class MainApplication : Application() {
}