package news.agoda.com.sample.util

import android.content.Context
import android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE
import android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK
import android.net.ConnectivityManager


class AppUtils {
    companion object {
        @JvmStatic
        fun isTablet(context: Context): Boolean {
            val xlarge =
                context.resources.configuration.screenLayout and SCREENLAYOUT_SIZE_MASK == 4
            val large =
                context.resources.configuration.screenLayout and SCREENLAYOUT_SIZE_MASK == SCREENLAYOUT_SIZE_LARGE
            return xlarge || large
        }

        @JvmStatic
        fun isNetworkAvailable(context: Context?): Boolean {
            val connectivityManager =
                context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }
}