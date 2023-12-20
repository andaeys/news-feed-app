package andaeys.io.newsapp.repository

import andaeys.io.newsapp.api.ApiConstant
import andaeys.io.newsapp.api.ApiService
import andaeys.io.newsapp.model.state.NewsStateStatus
import andaeys.io.newsapp.model.state.TopNewsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TopNewsRepositoryImpl(private val apiService: ApiService) : TopNewsRepository {
    override suspend fun fetchTopNews(): Flow<TopNewsState> = flow {
        emit(TopNewsState(status = NewsStateStatus.LOADING))

        try {
            val response = apiService.getTopHeadlines(
                ApiConstant.COUNTRY,
                ApiConstant.API_KEY
            )

            val topNewsState = when (response.status) {
                "ok" -> {
                    TopNewsState(
                        status = if (response.articles.isNotEmpty()) NewsStateStatus.SUCCESS else NewsStateStatus.EMPTY ,
                        totalArticle = response.totalResults?:0,
                        articleList = response.articles
                    )
                }
                "error" -> {
                    TopNewsState(
                        status = NewsStateStatus.FAILED,
                        errorMessage = "Error fetch top news"
                    )
                }
                else -> {
                    TopNewsState(
                        status = NewsStateStatus.FAILED,
                        errorMessage = "unknown error"
                    )
                }
            }

            emit(topNewsState)
        } catch (e: Exception) {
            emit(
                TopNewsState(
                    status = NewsStateStatus.FAILED,
                    errorMessage = e.message ?: ""
                )
            )
        }
    }
}
