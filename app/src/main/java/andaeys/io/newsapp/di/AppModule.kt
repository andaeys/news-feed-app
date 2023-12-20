package andaeys.io.newsapp.di

import andaeys.io.newsapp.api.ApiService
import andaeys.io.newsapp.api.RetrofitBuilder
import andaeys.io.newsapp.repository.TopNewsRepository
import andaeys.io.newsapp.repository.TopNewsRepositoryImpl
import retrofit2.Retrofit

import org.koin.dsl.module

val networkModule = module {
    single { RetrofitBuilder.build() }

    single { get<Retrofit>().create(ApiService::class.java) }
}

val repositoryModule = module {
    single<TopNewsRepository> {TopNewsRepositoryImpl(get())}
}
