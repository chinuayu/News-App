package news.agoda.com.sample.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import news.agoda.com.sample.fragments.NewsListViewModel
import news.agoda.com.sample.main.MainActivitySharedViewModel
import news.agoda.com.sample.util.ViewModelKey
import javax.inject.Singleton

@Module
abstract class ViewModelModule {

    @Binds
    @Singleton
    @IntoMap
    @ViewModelKey(NewsListViewModel::class)
    abstract fun bindListViewModel(movieListViewModel: NewsListViewModel): ViewModel

    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(MainActivitySharedViewModel::class)
    abstract fun bindMainActivitySharedViewModel(mainActivitySharedViewModel: MainActivitySharedViewModel): ViewModel


    @Binds
    @Singleton
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
