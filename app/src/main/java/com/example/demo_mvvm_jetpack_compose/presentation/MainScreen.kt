package com.example.demo_mvvm_jetpack_compose.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

enum class MainScreen(
    val icon: ImageVector? = null
) {
    SplashScreen,
    // all tag from quote api
    QuoteTag(
        icon = Icons.Filled.Info,
    ),

    // random quote with remoteMediator as paging
    RandomQuote(
        icon = Icons.Filled.Home
    ),

    // demo search quote by keywords with remoteMediator as paging
    SearchQuote(
        icon = Icons.Filled.Search
    );

    companion object {
        fun fromRoute(route: String?): MainScreen =
            when (route?.substringBefore("/")) {
                QuoteTag.name -> QuoteTag
                RandomQuote.name -> RandomQuote
                SearchQuote.name -> SearchQuote
                SplashScreen.name -> SplashScreen
                null -> QuoteTag
                else -> throw IllegalArgumentException("Route $route is not recognized")
            }
    }
}
