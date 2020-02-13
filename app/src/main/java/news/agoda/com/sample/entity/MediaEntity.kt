package news.agoda.com.sample.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * This class represents a media item
 */
@Parcelize
data class MediaEntity(
    @SerializedName("url")
    val url: String,
    @SerializedName("format")
    val format: String,
    @SerializedName("height")
    val height: Int,
    @SerializedName("width")
    val width: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("subtype")
    val subtype: String,
    @SerializedName("caption")
    val caption: String,
    @SerializedName("copyright")
    val copyright: String
) : Parcelable
