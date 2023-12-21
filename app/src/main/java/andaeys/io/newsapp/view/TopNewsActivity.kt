package andaeys.io.newsapp.view

import andaeys.io.newsapp.model.Article
import andaeys.io.newsapp.model.state.TopNewsState
import andaeys.io.newsapp.ui.theme.TopHeadlineNewsTheme
import andaeys.io.newsapp.viewmodels.TopNewsViewModel
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.androidx.viewmodel.ext.android.viewModel

class TopNewsActivity : ComponentActivity() {

    private val viewModel: TopNewsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TopHeadlineNewsTheme {
                val topNewsState by viewModel.topNewsState.collectAsState()
                TopNewsScreen(
                    state = topNewsState,
                    onRefresh = {
                        viewModel.fetchTopNews()
                    }
                )
            }

        }
    }

    override fun onResume() {
        super.onResume()
    }
}

@Composable
fun TopNewsScreen(
    state: TopNewsState,
    onArticleClick: (Article) -> Unit = {},
    onRefresh: () -> Unit = {}
) {

    LaunchedEffect(Unit){
        onRefresh()
    }
    Log.d("hello", state.toString())
}

