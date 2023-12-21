package andaeys.io.newsapp

import andaeys.io.newsapp.di.domainModule
import andaeys.io.newsapp.di.networkModule
import andaeys.io.newsapp.di.repositoryModule
import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NewsApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@NewsApplication)
            modules(networkModule, repositoryModule, domainModule)
        }
    }
}
