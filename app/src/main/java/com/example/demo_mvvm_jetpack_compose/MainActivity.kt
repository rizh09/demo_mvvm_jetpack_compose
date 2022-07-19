package com.example.demo_mvvm_jetpack_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.demo_mvvm_jetpack_compose.ui.components.MainTabRow
import com.example.demo_mvvm_jetpack_compose.ui.list.ListBody
import com.example.demo_mvvm_jetpack_compose.ui.list.SingleQuoteResultDetail
import com.example.demo_mvvm_jetpack_compose.ui.theme.Demo_mvvm_jetpack_composeTheme
import com.example.demo_mvvm_jetpack_compose.viewmodel.QuoteResultViewModel
import dagger.hilt.android.AndroidEntryPoint


//Hilt can provide dependencies to other Android classes that have the @AndroidEntryPoint annotation:
//If you annotate an Android class with @AndroidEntryPoint, then you also must annotate Android classes that depend on it. For example, if you annotate a fragment, then you must also annotate any activities where you use that fragment.
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        navController = navController, startDestination = MainScreen.QuoteResultList.name,
        modifier = modifier
    ) {
        composable(route = MainScreen.QuoteResultList.name) {
            val viewModel = hiltViewModel<QuoteResultViewModel>()
            ListBody(viewModel = viewModel) { quoteID ->
                navigateToSingleQuote(navController = navController, quoteID = quoteID)
            }
        }

        val quoteResultsName = MainScreen.QuoteResultList.name
        composable(
            route = "$quoteResultsName/{quoteID}",
            arguments = listOf(
                navArgument("quoteID") {
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
        )
        { entry ->
            val viewModel = hiltViewModel<QuoteResultViewModel>(entry)
            val quoteID = entry.arguments?.getString("quoteID")
            if (quoteID != null) {
                SingleQuoteResultDetail(quoteID = quoteID, viewModel = viewModel)
            }
        }
    }
}

private fun navigateToSingleQuote(navController: NavHostController, quoteID: String) {
    //create an route with QuoteResultList/quoteID
    navController.navigate("${MainScreen.QuoteResultList.name}/$quoteID")
}
