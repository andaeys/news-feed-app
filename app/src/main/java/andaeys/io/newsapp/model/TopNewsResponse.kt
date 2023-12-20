package andaeys.io.newsapp.model

data class TopNewsResponse(
    val articles: List<Article>,
    val status: String?,
    val totalResults: Int?
)