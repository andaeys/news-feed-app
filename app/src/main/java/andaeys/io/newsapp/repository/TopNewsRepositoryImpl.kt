package andaeys.io.newsapp.repository

import andaeys.io.newsapp.api.ApiConstant
import andaeys.io.newsapp.api.ApiService
import andaeys.io.newsapp.model.TopNews

class TopNewsRepositoryImpl(private val apiService: ApiService) : TopNewsRepository {

    override suspend fun fetchTopNews(): TopNews {
        val response = apiService.getTopHeadlines(ApiConstant.COUNTRY, ApiConstant.API_KEY)
        if (response.status!="ok") {
            throw Exception("Error fetching top news")
        }
        return TopNews(
            articles = response.articles.filter { it.title!="[Removed]" }
        )
    }
}
