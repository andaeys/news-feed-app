package andaeys.io.newsapp.domain

import andaeys.io.newsapp.model.TopNews
import andaeys.io.newsapp.model.TopNewsResponse
import andaeys.io.newsapp.model.state.TopNewsState
import andaeys.io.newsapp.repository.TopNewsRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
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
        val expectedFLowCount = 2

        //stubbing
        `when`(repository.fetchTopNews()).thenReturn(expectedTopNews)

        val result = fetchTopNews.execute().toList()

        //assertion
        assertEquals(expectedFLowCount, result.size)
        assertTrue(result[0] is TopNewsState.Loading)
        assertTrue(result[1] is TopNewsState.Success)
        val successState = (result[1] as TopNewsState.Success)
        assertEquals(expectedTopNews.articles.size, successState.totalArticle)
        assertTrue(expectedTopNews.articles.contains(successState.articleList.first()))
    }

    @Test
    fun `execute should return success with filtered articles when fetch is contain removed article title `() = runBlocking {
        //define expected result
        val jsonString = javaClass.classLoader?.getResource("topnews_removed.json")?.readText()
        val expectedResponse = Gson().fromJson(jsonString, TopNewsResponse::class.java)
        val expectedTopNews = TopNews(expectedResponse.articles)
        val expectedFLowCount = 2
        val expectedArticleCount = 1

        //stubbing
        `when`(repository.fetchTopNews()).thenReturn(expectedTopNews)

        val result = fetchTopNews.execute().toList()

        //assertion
        assertEquals(expectedFLowCount, result.size)
        assertTrue(result[0] is TopNewsState.Loading)
        assertTrue(result[1] is TopNewsState.Success)
        val successState = (result[1] as TopNewsState.Success)
        assertEquals(expectedArticleCount, successState.totalArticle)
        assertTrue(expectedTopNews.articles.contains(successState.articleList.first()))
    }

    @Test
    fun `execute should return success with empty state when fetch return empty data `() = runBlocking {
        //define expected result
        val jsonString = javaClass.classLoader?.getResource("topnews_empty.json")?.readText()
        val expectedResponse = Gson().fromJson(jsonString, TopNewsResponse::class.java)
        val expectedTopNews = TopNews(expectedResponse.articles)
        val expectedFLowCount = 2

        //stubbing
        `when`(repository.fetchTopNews()).thenReturn(expectedTopNews)

        val result = fetchTopNews.execute().toList()

        //assertion
        assertEquals(expectedFLowCount, result.size)
        assertTrue(result[0] is TopNewsState.Loading)
        assertTrue(result[1] is TopNewsState.Empty)
    }

    @Test
    fun `execute should return Error state when fetch has failed`() = runBlocking {
        //define expected result
        val expectedError = Exception("Error fetch")
        val expectedFLowCount = 2

        //stubbing
        `when`(repository.fetchTopNews()).thenAnswer { throw expectedError }

        val result = fetchTopNews.execute().toList()

        //assertion
        assertEquals(expectedFLowCount, result.size)
        assertTrue(result[0] is TopNewsState.Loading)
        assertTrue(result[1] is TopNewsState.Error)
        val errorState = (result[1] as TopNewsState.Error)
        assertEquals(expectedError.message, errorState.errorMessage)
    }
}
