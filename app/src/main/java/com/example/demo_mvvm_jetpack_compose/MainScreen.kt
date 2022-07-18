package com.example.demo_mvvm_jetpack_compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.vector.ImageVector

enum class MainScreen(
    val icon: ImageVector,
) {
    QuoteResultList(
        icon = Icons.Filled.Menu,
    );

    companion object {
        fun fromRoute(route: String?): MainScreen =
            when (route?.substringBefore("/")) {
                QuoteResultList.name -> QuoteResultList
                null -> QuoteResultList
                else -> throw IllegalArgumentException("Route $route is not recognized")
            }
    }
}
