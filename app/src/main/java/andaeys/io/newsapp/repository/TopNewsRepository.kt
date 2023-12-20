package andaeys.io.newsapp.repository

import andaeys.io.newsapp.model.state.TopNewsState
import kotlinx.coroutines.flow.Flow

interface TopNewsRepository {
    suspend fun fetchTopNews(): Flow<TopNewsState>
}
