package andaeys.io.newsapp.viewmodels

import andaeys.io.newsapp.domain.FetchTopNews
import andaeys.io.newsapp.model.state.TopNewsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TopNewsViewModel(private val fetchTopNews: FetchTopNews) : ViewModel() {

    private val _topNewsState = MutableStateFlow<TopNewsState>(TopNewsState.Loading)
    val topNewsState: StateFlow<TopNewsState> = _topNewsState

    fun fetchTopNews() {
        viewModelScope.launch {
           _topNewsState.value = TopNewsState.Loading
            try {
                val topNews = fetchTopNews.execute()
                val topNewsState: TopNewsState = if (topNews.articles.isNotEmpty()) {
                    TopNewsState.Success(
                        articleList = topNews.articles,
                        totalArticle = topNews.articles.size
                    )
                } else {
                    TopNewsState.Empty
                }
               _topNewsState.value = topNewsState
            } catch (e: Exception) {
                _topNewsState.value = TopNewsState.Error(errorMessage = e.message?:"unknown error")
            }
        }
    }
}
