package news.agoda.com.sample.util

import androidx.annotation.StringDef

@Retention(AnnotationRetention.SOURCE)
@StringDef(
    FormatEnums.STANDARD_THUMBNAIL,
    FormatEnums.THUMB_LARGE,
    FormatEnums.NORMAL,
    FormatEnums.TABLET
)
annotation class FormatEnums {
    companion object {
        const val STANDARD_THUMBNAIL = "Standard Thumbnail"
        const val THUMB_LARGE = "thumbLarge"
        const val NORMAL = "Normal"
        const val TABLET = "mediumThreeByTwo210"
    }
}