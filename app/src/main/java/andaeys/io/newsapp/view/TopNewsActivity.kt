package andaeys.io.newsapp.view

import andaeys.io.newsapp.model.Article
import andaeys.io.newsapp.model.state.TopNewsState
import andaeys.io.newsapp.ui.theme.TopHeadlineNewsTheme
import andaeys.io.newsapp.viewmodels.TopNewsViewModel
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
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
                title = { Text("Top News") },
                actions = {
                    IconButton(onClick = { onRefresh() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        },
        content = {
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
        CircularProgressIndicator()
    }
}

@Composable
fun ArticleList(articles: List<Article>, onArticleClick: (Article) -> Unit) {
    LazyColumn {
        items(articles) { article ->
            ArticleListItem(article = article, onArticleClick = { onArticleClick(article) })
            Divider(modifier = Modifier.padding(horizontal = 16.dp), color = Color.Gray)
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
        Text(text = article.title, style = typography.titleMedium)
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
            Text("Error: $errorMessage")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { onRetry() }) {
                Text("Retry")
            }
        }
    }
}