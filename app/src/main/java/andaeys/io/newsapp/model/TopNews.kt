package andaeys.io.newsapp.model

data class TopNews(
    val articles: List<Article> = emptyList(),
    val totalResults: Int=0
)
