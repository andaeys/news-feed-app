package andaeys.io.newsapp.viewmodels

import andaeys.io.newsapp.domain.FetchTopNews
import andaeys.io.newsapp.model.state.TopNewsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class TopNewsViewModel(private val fetchTopNews: FetchTopNews) : ViewModel() {

    private val _topNewsState = MutableStateFlow<TopNewsState>(TopNewsState.Loading)
    val topNewsState: StateFlow<TopNewsState> = _topNewsState

    fun fetchTopNews() {
        viewModelScope.launch {
            dummyStateFlow().collect { result ->
                _topNewsState.value = result
            }
        }
    }

    //TODO: remove once real state logic implemented
    private fun dummyStateFlow() = flow { emit(TopNewsState.Loading)}
}
