package news.agoda.com.sample.base

import android.app.Application
import news.agoda.com.sample.di.component.ApplicationComponent
import news.agoda.com.sample.di.component.DaggerApplicationComponent

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        component = DaggerApplicationComponent.builder().application(this).build()
        component.inject(this)
    }

    companion object {
        lateinit var component: ApplicationComponent
        fun getApplicationComponent(): ApplicationComponent {
            return component
        }
    }
}
