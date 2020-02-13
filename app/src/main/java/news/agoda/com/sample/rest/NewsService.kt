package news.agoda.com.sample.rest

import io.reactivex.Observable
import news.agoda.com.sample.responses.NewsResponse
import retrofit2.http.GET

interface NewsService {

    @GET("https://api.myjson.com/bins/nl6jh")
    fun getNewsList(): Observable<NewsResponse>


    @GET("http://www.mocky.io/v2/573c89f31100004a1daa8adb")
    fun getFallbackNewsList(): Observable<NewsResponse>

}
