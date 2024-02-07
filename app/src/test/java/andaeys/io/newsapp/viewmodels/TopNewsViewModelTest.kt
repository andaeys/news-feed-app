package andaeys.io.newsapp.viewmodels

import andaeys.io.newsapp.domain.FetchTopNews
import andaeys.io.newsapp.model.TopNews
import andaeys.io.newsapp.model.TopNewsResponse
import andaeys.io.newsapp.model.state.TopNewsState
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class TopNewsViewModelTest {

    private lateinit var viewModel: TopNewsViewModel

    @Mock
    private lateinit var fetchTopNews: FetchTopNews

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        MockitoAnnotations.openMocks(this)
        viewModel = TopNewsViewModel(fetchTopNews)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should return flow of state with loading and news data when fetch is success`()  = runTest{
        //define expected result
        val jsonString = javaClass.classLoader?.getResource("topnews.json")?.readText()
        val expectedResponse = Gson().fromJson(jsonString, TopNewsResponse::class.java)
        val expectedTopNews = TopNews(expectedResponse.articles)
        val expectedFlowCount = 2

        //stubbing
        `when`(fetchTopNews.execute()).thenReturn(expectedTopNews)

        //execution
        val states = mutableListOf<TopNewsState>()
        val job = launch {
            viewModel.topNewsState.toList(states)
        }
        viewModel.fetchTopNews()
        advanceUntilIdle()

        //assertion
        assertEquals(expectedFlowCount, states.size)
        assertTrue(states[0] is TopNewsState.Loading)
        job.cancel()
        assertTrue(states[1] is TopNewsState.Success)
        if (states[1] is TopNewsState.Success){
            val successState = states[1] as TopNewsState.Success
            assertEquals(expectedTopNews.articles.size, successState.totalArticle)
            assertTrue(expectedTopNews.articles.contains(successState.articleList.first()))
        }

    }

    @Test
    fun `should return flow of state with loading and empty when fetch is success with empty result`() = runTest {
        //define expected result
        val jsonString = javaClass.classLoader?.getResource("topnews_empty.json")?.readText()
        val expectedResponse = Gson().fromJson(jsonString, TopNewsResponse::class.java)
        val expectedTopNews = TopNews(expectedResponse.articles)
        val expectedFlowCount = 2

        //stubbing
        `when`(fetchTopNews.execute()).thenReturn(expectedTopNews)

        //execution
        val states = mutableListOf<TopNewsState>()
        val job = launch {
            viewModel.topNewsState.toList(states)
        }
        viewModel.fetchTopNews()
        advanceUntilIdle()

        //assertion
        assertEquals(expectedFlowCount, states.size)
        assertTrue(states[0] is TopNewsState.Loading)
        job.cancel()
        assertTrue(states[1] is TopNewsState.Empty)
    }

    @Test
    fun `should return flow of state with loading and error when fetch is failed`() = runTest {
        val expectedErrorMessage = "Network Error"
        val expectedFlowCount = 2

        //stubbing
        `when`(fetchTopNews.execute()).thenThrow(RuntimeException(expectedErrorMessage))

        //execution
        val states = mutableListOf<TopNewsState>()
        val job = launch {
            viewModel.topNewsState.toList(states)
        }
        viewModel.fetchTopNews()
        advanceUntilIdle()

        //assertion
        assertEquals(expectedFlowCount, states.size)
        assertTrue(states[0] is TopNewsState.Loading)
        job.cancel()
        assertTrue(states[1] is TopNewsState.Error)
        if(states[1] is TopNewsState.Error){
            val errorState = states[1] as TopNewsState.Error
            assertEquals(expectedErrorMessage, errorState.errorMessage)
        }
    }
}
