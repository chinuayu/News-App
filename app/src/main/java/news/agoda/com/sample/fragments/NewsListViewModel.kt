package news.agoda.com.sample.fragments

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import news.agoda.com.sample.entity.NewsEntity
import news.agoda.com.sample.responses.NewsResponse
import news.agoda.com.sample.rest.NewsService
import javax.inject.Inject

class NewsListViewModel @Inject
constructor(private val newsService: NewsService) : ViewModel() {
    private var disposable: CompositeDisposable = CompositeDisposable()

    private var allNews: MutableLiveData<List<NewsEntity>> = MutableLiveData()
    private var loading = MutableLiveData<Boolean>()
    private var errorReceived = MutableLiveData<Boolean>()

    fun getNews(): LiveData<List<NewsEntity>> {
        return allNews
    }

    init {
        loading.value = true
        errorReceived.value = false
    }

    fun getLoading(): LiveData<Boolean> {
        return loading
    }

    fun getErrorReceived(): LiveData<Boolean> {
        return errorReceived
    }

    fun fetchNews() {
        disposable.add(
            newsService.getNewsList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap { t -> return@flatMap getFilteresdNewsObject(t) }
                .retry(3)
                .onErrorResumeNext(callToFallbackNewsService())
                .subscribe({ onSuccess(it) }, { onError(it) })
        )
    }

    private fun callToFallbackNewsService(): Observable<List<NewsEntity>> {
        return newsService.getFallbackNewsList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { t -> return@flatMap getFilteresdNewsObject(t) }
    }

    private fun getFilteresdNewsObject(newsResponse: NewsResponse): Observable<List<NewsEntity>> {
        val newsList = mutableListOf<NewsEntity>()
        for (newsEntity in newsResponse.results) {
            if (!newsEntity.multimedia.isNullOrEmpty()) {
                newsList.add(newsEntity)
            }
        }
        return Observable.just(newsList)
    }


    private fun onSuccess(list: List<NewsEntity>) {
        Log.d("Wrapped Server Response", list.toString())
        loading.value = false
        if (list.isNotEmpty()) {
            allNews.value = list
        } else {
            allNews.value = emptyList()
        }
    }

    private fun onError(it: Throwable) {
        Log.e("Error", it.toString())
        errorReceived.value = true
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
