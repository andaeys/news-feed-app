package andaeys.io.newsapp.domain

import andaeys.io.newsapp.model.TopNews
import andaeys.io.newsapp.model.TopNewsResponse
import andaeys.io.newsapp.repository.TopNewsRepository
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class FetchTopNewsTest {

    @Mock
    private lateinit var repository: TopNewsRepository

    private lateinit var fetchTopNews: FetchTopNews

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        fetchTopNews = FetchTopNewsImpl(repository)
    }

    @Test
    fun `execute should return success with data when fetch is finished `() = runBlocking {
        //define expected result
        val jsonString = javaClass.classLoader?.getResource("topnews.json")?.readText()
        val expectedResponse = Gson().fromJson(jsonString, TopNewsResponse::class.java)
        val expectedTopNews = TopNews(expectedResponse.articles)

        //stubbing
        `when`(repository.fetchTopNews()).thenReturn(expectedTopNews)

        val result = fetchTopNews.execute()

        //assertion
        assertEquals(expectedTopNews.articles.size, result.articles.size)
        assertTrue(expectedTopNews.articles.contains(result.articles.first()))
    }

    @Test
    fun `execute should return success with filtered articles when fetch is contain removed article title `() = runBlocking {
        //define expected result
        val jsonString = javaClass.classLoader?.getResource("topnews_removed.json")?.readText()
        val expectedResponse = Gson().fromJson(jsonString, TopNewsResponse::class.java)
        val expectedTopNews = TopNews(expectedResponse.articles)
        val expectedArticleCount = 1

        //stubbing
        `when`(repository.fetchTopNews()).thenReturn(expectedTopNews)

        val result = fetchTopNews.execute()

        //assertion
        assertEquals(expectedArticleCount, result.articles.size)
        assertTrue(expectedTopNews.articles.contains(result.articles.first()))
    }

    @Test
    fun `execute should return success with empty state when fetch return empty data `() = runBlocking {
        //define expected result
        val jsonString = javaClass.classLoader?.getResource("topnews_empty.json")?.readText()
        val expectedResponse = Gson().fromJson(jsonString, TopNewsResponse::class.java)
        val expectedTopNews = TopNews(expectedResponse.articles)
        val expectedResultSize = 0

        //stubbing
        `when`(repository.fetchTopNews()).thenReturn(expectedTopNews)

        val result = fetchTopNews.execute()

        //assertion
        assertEquals(expectedResultSize, result.articles.size)
    }

    @Test
    fun `execute should return Error state when fetch has failed`() = runBlocking {
        //define expected result
        val expectedError = Exception("Error fetch")

        //stubbing
        `when`(repository.fetchTopNews()).thenAnswer { throw expectedError }

        val exception = assertThrows(Exception::class.java){
            runBlocking { repository.fetchTopNews() }
        }

        //assertion
        assertEquals(expectedError.message, exception.message)
    }
}
