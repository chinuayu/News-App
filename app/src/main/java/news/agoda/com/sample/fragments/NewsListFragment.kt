package news.agoda.com.sample.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import news.agoda.com.sample.R
import news.agoda.com.sample.adapters.NewsListAdapter
import news.agoda.com.sample.base.BaseApplication
import news.agoda.com.sample.base.BaseFragment
import news.agoda.com.sample.databinding.NewsListFragmentBinding
import news.agoda.com.sample.di.module.ViewModelFactory
import news.agoda.com.sample.entity.NewsEntity
import news.agoda.com.sample.main.MainActivity
import news.agoda.com.sample.main.MainActivitySharedViewModel
import news.agoda.com.sample.util.AppUtils
import javax.inject.Inject


class NewsListFragment : BaseFragment(), NewsListAdapter.NewsClickListener {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var newsListViewModel: NewsListViewModel
    private lateinit var mainActivitySharedViewModel: MainActivitySharedViewModel

    private lateinit var newsAdapter: NewsListAdapter
    private lateinit var binding: NewsListFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.news_list_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.loadingView.visibility = View.VISIBLE

        BaseApplication.getApplicationComponent().inject(this)

        initViewModels()

        initRecyclerViewAndAdapter()

        observableViewModel()
    }

    private fun initRecyclerViewAndAdapter() {
        context?.let {
            newsAdapter = NewsListAdapter(context!!, this)
            binding.recyclerView.layoutManager = LinearLayoutManager(context)
            binding.recyclerView.itemAnimator = DefaultItemAnimator()
        }
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                activity!!,
                DividerItemDecoration.VERTICAL
            )
        )

        binding.recyclerView.adapter = newsAdapter

    }

    private fun initViewModels() {

        newsListViewModel =
            ViewModelProvider(this, viewModelFactory).get(NewsListViewModel::class.java)

        mainActivitySharedViewModel =
            ViewModelProvider(activity as MainActivity, viewModelFactory)
                .get(MainActivitySharedViewModel::class.java)


        if (AppUtils.isNetworkAvailable(context)) {
            newsListViewModel.fetchNews()
        } else {
            takeNoInternetAction()
        }
    }

    private fun takeNoInternetAction() {
        decideVisibility(binding.loadingView, View.GONE)
        decideVisibility(binding.recyclerView, View.GONE)
        decideVisibility(binding.tvError, View.VISIBLE)
        binding.tvError.text = getString(R.string.no_internet_error_message)
    }

    private fun decideVisibility(view: View, visibility: Int) {
        view.visibility = visibility
    }

    private fun observableViewModel() {
        newsListViewModel.getNews().observe(viewLifecycleOwner, Observer {
            if (it != null && it.isNotEmpty()) {
                decideVisibility(binding.loadingView, View.GONE)
                decideVisibility(binding.tvError, View.GONE)
                decideVisibility(binding.recyclerView, View.VISIBLE)
                newsAdapter.setNewsData(it)
            } else if (binding.loadingView.visibility == View.GONE) {
                decideVisibility(binding.recyclerView, View.GONE)
                decideVisibility(binding.tvError, View.VISIBLE)
                binding.tvError.text = getString(R.string.error_message)
            }
        })

        newsListViewModel.getErrorReceived().observe(viewLifecycleOwner, Observer {
            decideVisibility(binding.tvError, View.VISIBLE)
            decideVisibility(binding.recyclerView, View.GONE)
            decideVisibility(binding.loadingView, View.GONE)
            binding.tvError.text = getString(R.string.error_message)
        })

        newsListViewModel.getLoading().observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading != null) {
                binding.loadingView.visibility = if (isLoading) View.VISIBLE else View.GONE
                if (isLoading) {
                    decideVisibility(binding.recyclerView, View.GONE)
                    decideVisibility(binding.tvError, View.GONE)
                }
            }
        })
    }

    override fun onNewsClick(news: NewsEntity) {
        mainActivitySharedViewModel.setOpenDetailsFragment(news)
    }
}
