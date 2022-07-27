package com.example.demo_mvvm_jetpack_compose.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

enum class MainScreen(
    val icon: ImageVector,
) {
    QuoteResultList(
        icon = Icons.Filled.Menu,
    ),
    QuoteResultPaging(
        icon = Icons.Filled.Home
    ),
    QuotResultPagingWithSearch(
        icon = Icons.Filled.Search
    );

    companion object {
        fun fromRoute(route: String?): MainScreen =
            when (route?.substringBefore("/")) {
                QuoteResultList.name -> QuoteResultList
                QuoteResultPaging.name -> QuoteResultPaging
                QuotResultPagingWithSearch.name -> QuotResultPagingWithSearch
                null -> QuoteResultList
                else -> throw IllegalArgumentException("Route $route is not recognized")
            }
    }
}
