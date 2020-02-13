package news.agoda.com.sample.di.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import me.ayushig.viewmodel.di.module.ContextModule
import news.agoda.com.sample.base.BaseApplication
import news.agoda.com.sample.detail.DetailViewActivity
import news.agoda.com.sample.di.module.ApplicationModule
import news.agoda.com.sample.fragments.NewsListFragment
import news.agoda.com.sample.main.MainActivity
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ContextModule::class, ApplicationModule::class]
)
interface ApplicationComponent {

    fun inject(application: BaseApplication)

    fun inject(mainActivity: MainActivity)

    fun inject(newsListFragment: NewsListFragment)

    fun inject(detailViewActivity: DetailViewActivity)


    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }
}