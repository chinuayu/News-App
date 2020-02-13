package news.agoda.com.sample

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.filters.SmallTest
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import news.agoda.com.sample.entity.MediaEntity
import news.agoda.com.sample.entity.NewsEntity
import news.agoda.com.sample.fragments.NewsListViewModel
import news.agoda.com.sample.responses.NewsResponse
import news.agoda.com.sample.rest.NewsService
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.util.concurrent.TimeUnit

@SmallTest
class NewsListViewModelTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()
    @Rule
    @JvmField
    val ruleForLivaData = InstantTaskExecutorRule()
    @Mock
    lateinit var newsService: NewsService

    lateinit var newsListViewModel: NewsListViewModel

    private val testScheduler = TestScheduler()

    @Mock
    lateinit var mockLiveDataObserver: Observer<List<NewsEntity>>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        RxAndroidPlugins.setInitMainThreadSchedulerHandler {
            Schedulers.trampoline()
        }

        newsListViewModel = NewsListViewModel(newsService)

        Mockito.`when`(newsService.getNewsList())
            .thenReturn(Observable.just(getSampleMockedNewsResponse(withEmptyMultimedia = false)))

        newsListViewModel.getNews().observeForever(mockLiveDataObserver)
    }

    private fun getSampleMockedNewsResponse(withEmptyMultimedia: Boolean): NewsResponse {
        return (
                NewsResponse(
                    status = "OK",
                    copyright = "Copyright (c) 2015 The New York Times Company. All Rights Reserved.",
                    section = "technology",
                    lastUpdated = "2015-08-18T10:15:06-05:00",
                    numResults = 1,
                    results = listOf(getSampledMockedNewsEntitity(withEmptyMultimedia))
                )
                )
    }

    private fun getSampledMockedNewsEntitity(withEmptyMultimedia: Boolean): NewsEntity {
        return NewsEntity(
            section = "Business Day",
            subsection = "",
            title = "Work Policies May Be Kinder, but Brutal Competition Isn’t",
            _abstract = "Top-tier employers may be changing their official policies in a nod to work-life balance, but brutal competition remains an inescapable component of workers’ daily lives.",
            url = "http://www.nytimes.com/2015/08/18/business/work-policies-may-be-kinder-but-brutal-competition-isnt.html",
            byline = "By NOAM SCHEIBER",
            itemType = "Article",
            updatedDate = "2015-08-17T22:10:02-5:00",
            createdDate = "2015-08-17T22:10:04-5:00",
            publishedDate = "2015-08-18T04:00:00-5:00",
            materialTypeFacet = "News",
            kicker = "",
            multimedia = getSampledMockedMediaEntity(withEmptyMultimedia)
        )

    }

    private fun getSampledMockedMediaEntity(withEmptyMultimedia: Boolean): List<MediaEntity?> {
        if (withEmptyMultimedia) {
            return emptyList()
        }
        return listOf(
            MediaEntity(
                url = "http://static01.nyt.com/images/2015/08/18/business/18EMPLOY/18EMPLOY-thumbStandard.jpg",
                format = "Standard Thumbnail",
                height = 75,
                width = 75,
                type = "image",
                subtype = "photo",
                caption = "People eating at the Brave Horse Tavern on the Amazon campus in Seattle in June.",
                copyright = "Matthew Ryan Williams for The New York Times"
            )
        )
    }

    //Basic check that receving the correct data
    @Test
    fun testMovieModelStateOnMoviesFetch() {
        assert(newsListViewModel.getLoading().value == true)
        assert(newsListViewModel.getErrorReceived().value == false)

        val testObserver = newsService.getNewsList()
            .subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .test()
            .assertNotTerminated()
            .assertNoErrors()
            .assertValueCount(0)

        testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)

        testObserver.assertValueCount(1)

        assert(
            testObserver.values()[0].results[0].title == getSampledMockedNewsEntitity(
                withEmptyMultimedia = true
            ).title
        )

        verify(mockLiveDataObserver, times(0)).onChanged(any())

    }

    @Test
    fun testMovieModelStateOnMoviesFiltering() {
        Mockito.`when`(newsService.getNewsList())
            .thenReturn(Observable.just(getSampleMockedNewsResponse(withEmptyMultimedia = true)))

        newsListViewModel.fetchNews()

        verify(mockLiveDataObserver, times(0))
            .onChanged(getSampleMockedNewsResponse(true).results)

    }
}