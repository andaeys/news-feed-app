package andaeys.io.newsapp.domain

import andaeys.io.newsapp.model.state.TopNewsState
import kotlinx.coroutines.flow.Flow

interface FetchTopNews {
    suspend fun execute(): Flow<TopNewsState>
}
