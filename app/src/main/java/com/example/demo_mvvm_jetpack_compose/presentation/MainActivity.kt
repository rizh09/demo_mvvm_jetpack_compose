package com.example.demo_mvvm_jetpack_compose.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.demo_mvvm_jetpack_compose.presentation.ui.components.MainTabRow
import com.example.demo_mvvm_jetpack_compose.presentation.ui.list.*
import com.example.demo_mvvm_jetpack_compose.presentation.ui.theme.Demo_mvvm_jetpack_composeTheme
import com.example.demo_mvvm_jetpack_compose.presentation.viewmodel.QuoteResultViewModel
import com.example.demo_mvvm_jetpack_compose.presentation.viewmodel.QuoteTagViewModel
import com.example.demo_mvvm_jetpack_compose.util.WorkerHelper
import dagger.hilt.android.AndroidEntryPoint


//Hilt can provide dependencies to other Android classes that have the @AndroidEntryPoint annotation:
//If you annotate an Android class with @AndroidEntryPoint, then you also must annotate Android classes that depend on it. For example, if you annotate a fragment, then you must also annotate any activities where you use that fragment.
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //init and schedule worker helper
        WorkerHelper(context = this).createWorkRequest(
            repeatIntervalInHours = 6,
            timeDelayInMinutes = 15
        )
        setContent {
            MainApp()
        }
    }
}

@Composable
fun MainApp() {
    Demo_mvvm_jetpack_composeTheme {
        val allScreens = MainScreen.values().toList()
        val navController = rememberNavController()
        val backstackEntry = navController.currentBackStackEntryAsState()
        val currentScreen = MainScreen.fromRoute(backstackEntry.value?.destination?.route)

        Scaffold(
            topBar = {
                MainTabRow(
                    allScreens = allScreens,
                    currentScreen = currentScreen,
                    onTabSelected = { screen -> navController.navigate(screen.name) })
            }
        ) { innerPadding ->
            MainNavHost(navController = navController, modifier = Modifier.padding(innerPadding))
        }
    }
}

@Composable
fun MainNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = MainScreen.SplashScreen.name,
        modifier = modifier
    ) {
        //splash screen
        composable(route = MainScreen.SplashScreen.name) {
            SplashScreen(navController = navController)
        }
        composable(route = MainScreen.QuoteTag.name) {
            val viewModel = hiltViewModel<QuoteTagViewModel>()
            ListScreen(viewModel = viewModel)
            { tagName ->
                navigateToListQuotePagingFromTagPage(
                    navController = navController,
                    tagName = tagName
                )
            }
        }

        val quoteTagName = MainScreen.QuoteTag.name
        //paging list for selected tag
        composable(
            route = "$quoteTagName/{tagName}",
            arguments = listOf(
                navArgument("tagName") {
                    // Make argument type safe
                    type = NavType.StringType
                }
            )
            //study later
//            deepLinks = listOf(
//                navDeepLink {
//                    uriPattern = ""
//                }
//            )
        ) { entry ->
            val viewModel = hiltViewModel<QuoteTagViewModel>(entry)
            val tagName = entry.arguments?.getString("tagName")
            if (tagName != null) {
                PagingByTagScreen(
                    selectedTag = tagName,
                    viewModel = viewModel,
                    onQuoteClick = { quoteID ->
                        navigateToSingleQuoteFromRandomPage(
                            navController = navController,
                            quoteID = quoteID
                        )
                    }
                )
            }
        }

        composable(route = MainScreen.RandomQuote.name) {
            val viewModel = hiltViewModel<QuoteResultViewModel>()
            PagingScreen(
                viewModel = viewModel,
                onQuoteClick = { quoteID ->
                    navigateToSingleQuoteFromRandomPage(
                        navController = navController,
                        quoteID = quoteID
                    )
                }
            )
        }

        val quotResultPaging = MainScreen.RandomQuote.name
        composable(
            route = "$quotResultPaging/{quoteID}",
            arguments = listOf(
                navArgument("quoteID") {
                    // Make argument type safe
                    type = NavType.StringType
                }
            )
        ) { entry ->
            val viewModel = hiltViewModel<QuoteResultViewModel>(entry)
            val quoteID = entry.arguments?.getString("quoteID")
            if (quoteID != null) {
                SingleQuoteResultDetail(quoteID = quoteID, viewModel = viewModel)
            }
        }

        composable(route = MainScreen.SearchQuote.name) {
            val viewModel = hiltViewModel<QuoteResultViewModel>()
            PagingWithSearchScreen(
                viewModel = viewModel,
                onQuoteClick = { quoteID ->
                    navigateToSingleQuoteFromSearchPage(
                        navController = navController,
                        quoteID = quoteID
                    )
                },
                onSearchClicked = { keywords ->
                    viewModel.searchByKeywords(keywords)
                }
            )
        }
        val quotResultPagingWithSearch = MainScreen.SearchQuote.name
        composable(
            route = "$quotResultPagingWithSearch/{quoteID}",
            arguments = listOf(
                navArgument("quoteID") {
                    // Make argument type safe
                    type = NavType.StringType
                }
            )
        ) { entry ->
            val viewModel = hiltViewModel<QuoteResultViewModel>(entry)
            val quoteID = entry.arguments?.getString("quoteID")
            if (quoteID != null) {
                SingleQuoteResultDetail(quoteID = quoteID, viewModel = viewModel)
            }
        }
    }
}

private fun navigateToListQuotePagingFromTagPage(
    navController: NavHostController,
    tagName: String
) {
    //create an route with QuoteResultList/quoteID
    navController.navigate("${MainScreen.QuoteTag.name}/$tagName")
}

//navigate to single quote from random page
private fun navigateToSingleQuoteFromRandomPage(navController: NavHostController, quoteID: String) {
    navController.navigate("${MainScreen.RandomQuote.name}/$quoteID")
}


//navigate to single quote from search page
private fun navigateToSingleQuoteFromSearchPage(
    navController: NavHostController,
    quoteID: String
) {
    navController.navigate("${MainScreen.SearchQuote.name}/$quoteID")
}
