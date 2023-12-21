package andaeys.io.newsapp.domain

import andaeys.io.newsapp.model.state.TopNewsState
import andaeys.io.newsapp.repository.TopNewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FetchTopNewsImpl(private val topNewsRepository: TopNewsRepository) : FetchTopNews {
    override suspend fun execute(): Flow<TopNewsState> = flow {
        emit(TopNewsState.Loading)
        try {
            val topNews = topNewsRepository.fetchTopNews()
            val topNewsState: TopNewsState = if (topNews.articles.isNotEmpty()) {
                TopNewsState.Success(articleList = topNews.articles)
            } else {
                TopNewsState.Empty
            }
            emit(topNewsState)
        } catch (e: Exception) {
            emit(
                TopNewsState.Error(errorMessage = e.message?:"unknown error")
            )
        }
    }
}
