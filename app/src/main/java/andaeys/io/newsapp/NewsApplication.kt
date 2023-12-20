package andaeys.io.newsapp

import andaeys.io.newsapp.di.networkModule
import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NewsApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@NewsApplication)
            modules(networkModule)
        }
    }
}
