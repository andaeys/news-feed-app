package andaeys.io.newsapp.repository

import andaeys.io.newsapp.api.ApiConstant
import andaeys.io.newsapp.api.ApiService
import andaeys.io.newsapp.model.TopNewsResponse
import com.google.gson.Gson
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
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
        val result = repository.fetchTopNews()

        //assertion
        assertTrue(expectedTotalArticle == result.articles.size)
        assertTrue(expectedResponse.articles.contains(result.articles.last()))
    }

    @Test
    fun `fetchTopNes should throw an error when status not ok`() = runBlocking {
        //define result
        val jsonString = javaClass.classLoader?.getResource("topnews_error.json")?.readText()
        val expectedResponse = Gson().fromJson(jsonString, TopNewsResponse::class.java)

        //stubbing
        `when`(apiService.getTopHeadlines(ApiConstant.COUNTRY, ApiConstant.API_KEY))
            .thenReturn(expectedResponse)

        //assertion
        val exception = assertThrows(Exception::class.java){
            runBlocking { repository.fetchTopNews() }
        }
        assertEquals("Error fetching top news", exception.message)
    }

    @Test
    fun `fetchTopNes should throw an error when api service is error`() = runBlocking {
        //define result
        val expectedError = Exception("Server Error")

        //stubbing
        `when`(apiService.getTopHeadlines(ApiConstant.COUNTRY, ApiConstant.API_KEY))
            .thenAnswer { throw expectedError }

        //assertion
        val exception = assertThrows(Exception::class.java){
            runBlocking { repository.fetchTopNews() }
        }
        assertEquals(expectedError.message, exception.message)
    }
}
