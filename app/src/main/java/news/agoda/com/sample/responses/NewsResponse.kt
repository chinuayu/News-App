package news.agoda.com.sample.responses

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import news.agoda.com.sample.entity.NewsEntity

/**
 * Raw movie service from server
 */

@Parcelize
data class NewsResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("copyright")
    val copyright: String,
    @SerializedName("section")
    val section: String,
    @SerializedName("last_updated")
    val lastUpdated: String,
    @SerializedName("num_results")
    val numResults: Int,
    @SerializedName("results")
    val results: List<NewsEntity> = emptyList()
) : Parcelable