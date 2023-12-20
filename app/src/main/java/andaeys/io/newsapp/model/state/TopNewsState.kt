package andaeys.io.newsapp.model.state

import andaeys.io.newsapp.model.Article

data class TopNewsState(
    val status: NewsStateStatus = NewsStateStatus.LOADING,
    val articleList: List<Article> = emptyList(),
    val totalArticle: Int = 0,
    val errorMessage:String = ""
)

enum class NewsStateStatus {
    LOADING, SUCCESS, EMPTY, FAILED
}
