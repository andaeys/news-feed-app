package andaeys.io.newsapp.di

import andaeys.io.newsapp.api.ApiService
import andaeys.io.newsapp.api.RetrofitBuilder
import andaeys.io.newsapp.domain.FetchTopNews
import andaeys.io.newsapp.domain.FetchTopNewsImpl
import andaeys.io.newsapp.repository.TopNewsRepository
import andaeys.io.newsapp.repository.TopNewsRepositoryImpl
import andaeys.io.newsapp.viewmodels.TopNewsViewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val networkModule = module {
    single { RetrofitBuilder.build() }

    single { get<Retrofit>().create(ApiService::class.java) }
}

val repositoryModule = module {
    single<TopNewsRepository> {TopNewsRepositoryImpl(get())}
}

val domainModule = module {
    single<FetchTopNews> {FetchTopNewsImpl(get())}
}

val viewModelModule = module {
    factory { TopNewsViewModel(get())}
}
