package com.example.demo_mvvm_jetpack_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import com.example.demo_mvvm_jetpack_compose.model.Quote
import com.example.demo_mvvm_jetpack_compose.ui.theme.Demo_mvvm_jetpack_composeTheme
import com.example.demo_mvvm_jetpack_compose.viewmodel.QuoteResultViewModel
import dagger.hilt.android.AndroidEntryPoint


//Hilt can provide dependencies to other Android classes that have the @AndroidEntryPoint annotation:
//If you annotate an Android class with @AndroidEntryPoint, then you also must annotate Android classes that depend on it. For example, if you annotate a fragment, then you must also annotate any activities where you use that fragment.
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val quoteResultViewModel: QuoteResultViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //ref : line 70 at https://github.com/google-developer-training/android-kotlin-fundamentals-apps/blob/master/RepositoryPattern/app/src/main/java/com/example/android/devbyteviewer/ui/DevByteFragment.kt
        quoteResultViewModel.localData.observe(this, Observer<List<Quote.Result>> { it ->
            //update UI
            setContent {
                Demo_mvvm_jetpack_composeTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        MessageGridListWithLazy(it)
                    }
                }
            }
        })
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageGridListWithLazy(quoteResult: List<Quote.Result>) {
    //it skips the forEach
    LazyVerticalGrid(
        cells = GridCells.Adaptive(128.dp),
        content = {
            items(quoteResult) { index ->
                Card(
                    backgroundColor = Color.Cyan,
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(),
                    elevation = 8.dp,
                ) {
                    MessageRow(number = index.author)
                }
            }
        })
}

@Composable
fun MessageRow(number: String) {
    Text(text = "$number")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Demo_mvvm_jetpack_composeTheme {

//        val mLists = listOf(
//            "C++", "C", "C#", "Java", "Kotlin", "Dart", "Python", "Javascript", "SpringBoot",
//            "XML", "Dart", "Node JS", "Typescript", "Dot Net", "GoLang", "MongoDb",
//        )
//        MessageGridListWithLazy(mLists)
    }
}
