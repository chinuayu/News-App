package news.agoda.com.sample.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import news.agoda.com.sample.R
import news.agoda.com.sample.databinding.NewsListItemBinding
import news.agoda.com.sample.entity.MediaEntity
import news.agoda.com.sample.entity.NewsEntity
import news.agoda.com.sample.util.AppUtils

class NewsListAdapter(
    private val mContext: Context,
    private val mOnClickListener: NewsClickListener
) : RecyclerView.Adapter<NewsListAdapter.NewsListViewHolder>() {

    lateinit var list: List<NewsEntity>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = NewsListItemBinding.inflate(layoutInflater, parent, false)
        return NewsListViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: NewsListViewHolder, position: Int) {
        val data: NewsEntity = list[position]
        val mediaEntity: List<MediaEntity?> = data.multimedia
        var thumbnailURL = ""
        if (mediaEntity.isNotEmpty()) {
            thumbnailURL = if (AppUtils.isTablet(mContext)) {
                mediaEntity[2]?.url!!
            } else {
                mediaEntity[0]?.url!!
            }
        }
        holder.listItemNewsBinding.newsTitle.text = data.title
        insertThumbnail(holder, thumbnailURL)
    }

    private fun insertThumbnail(holder: NewsListViewHolder, thumbnailURL: String) {
        Glide.with(mContext)
            .applyDefaultRequestOptions(
                RequestOptions()
                    .placeholder(R.drawable.place_holder)
            )
            .load(thumbnailURL.replace("http", "https"))
            .into(holder.listItemNewsBinding.newsItemImage)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setNewsData(newsData: List<NewsEntity>) {
        list = newsData
        notifyDataSetChanged()
    }

    // Provide a reference to the views for each data item
    inner class NewsListViewHolder(val listItemNewsBinding: NewsListItemBinding) :
        RecyclerView.ViewHolder(listItemNewsBinding.root), View.OnClickListener {

        init {
            this.listItemNewsBinding.root.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            mOnClickListener.onNewsClick(list[adapterPosition])
        }
    }


    interface NewsClickListener {
        fun onNewsClick(news: NewsEntity)
    }

    companion object {
        private val TAG = NewsListAdapter::class.java.simpleName
    }
}
