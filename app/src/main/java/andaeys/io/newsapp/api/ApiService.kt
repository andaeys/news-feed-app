package andaeys.io.newsapp.api

import andaeys.io.newsapp.BuildConfig
import andaeys.io.newsapp.model.TopNewsResponse

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = ApiConstant.COUNTRY,
        @Query("apiKey") apiKey: String = BuildConfig.NEWS_API_KEY,
        @Query("category") category: String= ApiConstant.CATEGORY,
    ):TopNewsResponse
}
