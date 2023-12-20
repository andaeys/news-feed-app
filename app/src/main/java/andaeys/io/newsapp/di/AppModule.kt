package andaeys.io.newsapp.di

import andaeys.io.newsapp.api.ApiService
import andaeys.io.newsapp.api.RetrofitBuilder
import retrofit2.Retrofit

import org.koin.dsl.module

val networkModule = module {
    single { RetrofitBuilder.build() }

    single { get<Retrofit>().create(ApiService::class.java) }
}
