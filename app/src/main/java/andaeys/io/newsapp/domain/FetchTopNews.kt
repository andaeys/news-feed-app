package andaeys.io.newsapp.domain

import andaeys.io.newsapp.model.TopNews

interface FetchTopNews {
    suspend fun execute(): TopNews
}
