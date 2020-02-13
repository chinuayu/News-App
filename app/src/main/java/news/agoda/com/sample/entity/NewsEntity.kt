package news.agoda.com.sample.entity

import android.os.Parcelable
import androidx.annotation.Nullable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * This represents a news item
 */

@Parcelize
data class NewsEntity(
    @SerializedName("section")
    val section: String,
    @SerializedName("subsection")
    val subsection: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("abstract")
    val _abstract: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("byline")
    val byline: String,
    @SerializedName("item_type")
    val itemType: String,
    @SerializedName("updated_date")
    val updatedDate: String,
    @SerializedName("created_date")
    val createdDate: String,
    @SerializedName("published_date")
    val publishedDate: String,
    @SerializedName("material_type_facet")
    val materialTypeFacet: String,
    @SerializedName("kicker")
    val kicker: String,
    @SerializedName("multimedia")
    @Nullable
    val multimedia: List<MediaEntity?> = emptyList()
) : Parcelable

