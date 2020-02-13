package news.agoda.com.sample.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import news.agoda.com.sample.entity.NewsEntity
import javax.inject.Inject

class MainActivitySharedViewModel @Inject
constructor() : ViewModel() {
    val openDetailsFragment = MutableLiveData<NewsEntity>()

    fun setOpenDetailsFragment(newsEntity: NewsEntity) {
        openDetailsFragment.value = newsEntity
    }
}