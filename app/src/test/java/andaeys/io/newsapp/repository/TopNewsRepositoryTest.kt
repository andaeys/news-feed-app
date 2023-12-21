package andaeys.io.newsapp.repository

import andaeys.io.newsapp.api.ApiConstant
import andaeys.io.newsapp.api.ApiService
import andaeys.io.newsapp.model.TopNewsResponse
import andaeys.io.newsapp.model.state.TopNewsState
import kotlinx.coroutines.runBlocking
import com.google.gson.Gson
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.toList
import org.junit.Assert
import org.junit.Assert.assertTrue

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class TopNewsRepositoryTest {

    @Mock
    private lateinit var apiService: ApiService

    private lateinit var repository: TopNewsRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = TopNewsRepositoryImpl(apiService)
    }

    @Test
    fun `fetchTopNes should return article list when success`() = runBlocking {
        //define result
        val jsonString = javaClass.classLoader?.getResource("topnews.json")?.readText()
        val expectedResponse = Gson().fromJson(jsonString, TopNewsResponse::class.java)
        val expectedTotalArticle = expectedResponse.totalResults

        //stubbing
        `when`(apiService.getTopHeadlines(ApiConstant.COUNTRY, ApiConstant.API_KEY))
            .thenReturn(expectedResponse)

        //executing
        val result = repository.fetchTopNews().toList()

        //assertion
        assertEquals(2, result.size)
        assertTrue(result[0] is TopNewsState.Loading)
        assertTrue(result[1] is TopNewsState.Success)
        assertEquals(expectedTotalArticle, (result[1] as TopNewsState.Success).totalArticle)
    }
}
