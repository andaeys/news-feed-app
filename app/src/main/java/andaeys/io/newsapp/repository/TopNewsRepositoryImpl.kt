package andaeys.io.newsapp.repository

import andaeys.io.newsapp.api.ApiService
import andaeys.io.newsapp.model.TopNews

class TopNewsRepositoryImpl(private val apiService: ApiService) : TopNewsRepository {

    override suspend fun fetchTopNews(): TopNews {
        val response = apiService.getTopHeadlines()
        if (response.status!="ok") {
            throw Exception("Error fetching top news")
        }
        return TopNews(
            articles = response.articles
        )
    }
}
