package andaeys.io.newsapp.domain

import andaeys.io.newsapp.model.TopNews
import andaeys.io.newsapp.repository.TopNewsRepository

class FetchTopNewsImpl(private val topNewsRepository: TopNewsRepository) : FetchTopNews {
    override suspend fun execute(): TopNews {
        val rawTopNews = topNewsRepository.fetchTopNews()
        return rawTopNews.copy(
            articles = rawTopNews.articles.filterNot { it.title=="[Removed]" }
        )
    }
}
