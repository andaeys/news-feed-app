package andaeys.io.newsapp.view

import andaeys.io.newsapp.R
import andaeys.io.newsapp.model.Article
import andaeys.io.newsapp.model.state.TopNewsState
import andaeys.io.newsapp.ui.theme.grayAccent
import andaeys.io.newsapp.utils.convertToTimeAgo
import andaeys.io.newsapp.viewmodels.TopNewsViewModel
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.koin.androidx.viewmodel.ext.android.viewModel

class TopNewsActivity : ComponentActivity() {

    private val viewModel: TopNewsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val topNewsState by viewModel.topNewsState.collectAsState()
            TopNewsScreen(
                state = topNewsState,
                onRefresh = {
                    viewModel.fetchTopNews()
                },
                onArticleClick = { article->
                    val intent = Intent(this, NewsDetailActivity::class.java)
                    intent.putExtra(EXTRA_URL, article.url)
                    startActivity(intent)
                }
            )
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNewsScreen(
    state: TopNewsState,
    onArticleClick: (Article) -> Unit = {},
    onRefresh: () -> Unit = {}
) {

    LaunchedEffect(Unit){
        onRefresh()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Top News") }
            )
        },
        content = {
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = false),
                onRefresh = { onRefresh() }
            ){
                when (state) {
                    is TopNewsState.Loading -> {
                        // Loading state
                        LoadingScreen()
                    }
                    is TopNewsState.Success -> {
                        // Display list of articles
                        ArticleList(state.articleList, onArticleClick)
                    }
                    is TopNewsState.Empty -> {
                        // Empty state
                        EmptyState()
                    }
                    is TopNewsState.Error -> {
                        // Error state
                        ErrorState(state.errorMessage) {
                            // Retry button
                            onRefresh()
                        }
                    }
                }
            }

        }
    )

}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        val aComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
        LottieAnimation(
            composition = aComposition ,
            iterations = LottieConstants.IterateForever
        )
    }
}

@Composable
fun ArticleList(articles: List<Article>, onArticleClick: (Article) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(top = 64.dp)
    ) {
        items(articles) { article ->
            ArticleListItem(article = article, onArticleClick = { onArticleClick(article) })
            Divider(modifier = Modifier.padding(horizontal = 16.dp), color = Color.LightGray)
        }
    }
}

@Composable
fun ArticleListItem(article: Article, onArticleClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onArticleClick() }
            .padding(16.dp)
    ) {
        Column {
            AsyncImage(
                model = article.urlToImage?:"",
                placeholder = null,
                error = null,
                contentDescription = article.description,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = article.source?.name?:"",
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = article.title, style = typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = convertToTimeAgo(article.publishedAt ?: ""),
                color = grayAccent,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.End)
            )
        }

    }
}

@Composable
fun EmptyState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("No top news available")
    }
}

@Composable
fun ErrorState(errorMessage: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Error: $errorMessage", textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { onRetry() }) {
                Text("Retry")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
//    ArticleListItem(article = dummyArticle(), onArticleClick = { })
}
