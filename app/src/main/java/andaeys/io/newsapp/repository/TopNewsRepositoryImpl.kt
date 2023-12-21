package andaeys.io.newsapp.repository

import andaeys.io.newsapp.api.ApiConstant
import andaeys.io.newsapp.api.ApiService
import andaeys.io.newsapp.model.state.TopNewsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TopNewsRepositoryImpl(private val apiService: ApiService) : TopNewsRepository {
    override suspend fun fetchTopNews(): Flow<TopNewsState> = flow {
        emit(TopNewsState.Loading)

        try {
            val response = apiService.getTopHeadlines(
                ApiConstant.COUNTRY,
                ApiConstant.API_KEY
            )

            val topNewsState: TopNewsState = when (response.status) {
                "ok" -> {
                    if (response.articles.isNotEmpty()){
                        TopNewsState.Success(
                            articleList = response.articles,
                            totalArticle = response.totalResults
                        )
                    } else {
                        TopNewsState.Empty
                    }
                }
                "error" -> {
                    TopNewsState.Error(
                        errorMessage = "Error fetch top news"
                    )
                }
                else -> {
                    TopNewsState.Error(
                        errorMessage = "unknown error"
                    )
                }
            }
            emit(topNewsState)
        } catch (e: Exception) {
            emit(
                TopNewsState.Error(
                    errorMessage = e.message ?: ""
                )
            )
        }
    }
}
