package news.agoda.com.sample.di.module

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import dagger.Module
import dagger.Provides
import news.agoda.com.sample.rest.NewsService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class ApplicationModule {

    companion object {
        const val THE_NEWS_BASE_URL = "https://api.myjson.com"
    }

    @Singleton
    @Provides
    internal fun provideRetrofit(): Retrofit {
        val gson = GsonBuilder()
            .registerTypeAdapterFactory(NewsDataTypeAdapterFactory())
            .create()

        return Retrofit.Builder().baseUrl(THE_NEWS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    internal fun provideRetrofitService(retrofit: Retrofit): NewsService {
        return retrofit.create(NewsService::class.java)
    }


    class NewsDataTypeAdapterFactory : TypeAdapterFactory {
        override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T> {
            val delegate = gson.getDelegateAdapter(this, type)
            val elementAdapter = gson.getAdapter(JsonElement::class.java)
            return object : TypeAdapter<T>() {
                @Throws(IOException::class)
                override fun write(out: JsonWriter, value: T) {
                    delegate.write(out, value)
                }

                @Throws(IOException::class)
                override fun read(`in`: JsonReader): T {
                    val jsonElement = elementAdapter.read(`in`)
                    if (jsonElement.isJsonObject && jsonElement.asJsonObject.has("multimedia") &&
                        jsonElement.asJsonObject.get("multimedia") !is JsonArray
                    ) {
                        jsonElement.asJsonObject.remove("multimedia")
                    }
                    return delegate.fromJsonTree(jsonElement)
                }
            }.nullSafe()
        }
    }


}
