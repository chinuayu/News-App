package news.agoda.com.sample.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import news.agoda.com.sample.R
import news.agoda.com.sample.base.BaseActivity
import news.agoda.com.sample.base.BaseApplication
import news.agoda.com.sample.databinding.ActivityDetailBinding
import news.agoda.com.sample.entity.NewsEntity
import news.agoda.com.sample.util.AppUtils
import news.agoda.com.sample.util.FormatEnums.Companion.NORMAL
import news.agoda.com.sample.util.FormatEnums.Companion.STANDARD_THUMBNAIL
import news.agoda.com.sample.util.FormatEnums.Companion.TABLET

/**
 * News detail view
 */
class DetailViewActivity : BaseActivity() {

    lateinit var data: NewsEntity

    private lateinit var detailActivityBinding: ActivityDetailBinding

    private var storyURL: String? = ""

    override fun layoutRes(): Int {
        return R.layout.activity_detail
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.sliding_in_left, R.anim.sliding_out_right)
        BaseApplication.getApplicationComponent().inject(this)
        initUI()
    }

    private fun initUI() {
        detailActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        val intent = intent
        intent?.let {
            intent.getParcelableExtra<NewsEntity>("news_entity")?.let {
                data = intent.getParcelableExtra("news_entity")
                setUpUIWithData(data)
            }
        }
    }

    private fun setUpUIWithData(data: NewsEntity) {
        storyURL = data.url
        detailActivityBinding.title.text = data.title
        detailActivityBinding.summaryContent.text = data._abstract
        var imageUrl: String? = null
        for (multimedia in data.multimedia) {
            if (multimedia?.format == NORMAL || multimedia?.format == STANDARD_THUMBNAIL) {
                imageUrl = multimedia.url
            }

            if (AppUtils.isTablet(this)) {
                if (multimedia?.format == TABLET) {
                    imageUrl = multimedia.url
                }
            }
        }

        loadImage(imageUrl)

    }

    private fun loadImage(imageUrl: String?) {
        Glide.with(this)
            .applyDefaultRequestOptions(
                RequestOptions()
                    .placeholder(R.drawable.place_holder)
            )
            .load(imageUrl!!.replace("http", "https"))
            .into(detailActivityBinding.newsImage)
    }

    fun onFullStoryClicked(view: View) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(storyURL)
        startActivity(intent)
    }
}
