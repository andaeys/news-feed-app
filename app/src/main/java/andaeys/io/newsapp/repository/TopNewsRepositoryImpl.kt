package andaeys.io.newsapp.repository

import andaeys.io.newsapp.api.ApiService
import andaeys.io.newsapp.model.TopNews
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TopNewsRepositoryImpl(private val apiService: ApiService) : TopNewsRepository {

    override suspend fun fetchTopNews(): TopNews = withContext(Dispatchers.IO) {
        val response = apiService.getTopHeadlines()
        if (response.status!="ok") {
            throw Exception("Error fetching top news")
        }
        TopNews(
            articles = response.articles
        )
    }
}
