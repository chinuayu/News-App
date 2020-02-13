package news.agoda.com.sample.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import news.agoda.com.sample.R
import news.agoda.com.sample.base.BaseActivity
import news.agoda.com.sample.base.BaseApplication
import news.agoda.com.sample.databinding.MainActivityBinding
import news.agoda.com.sample.detail.DetailViewActivity
import news.agoda.com.sample.di.module.ViewModelFactory
import news.agoda.com.sample.entity.NewsEntity
import news.agoda.com.sample.fragments.NewsListFragment
import javax.inject.Inject


class MainActivity : BaseActivity() {

    @Inject
    lateinit var viewmodelFactory: ViewModelFactory

    private lateinit var sharedViewModel: MainActivitySharedViewModel

    override fun layoutRes(): Int {
        return R.layout.main_activity
    }

    private lateinit var mainActivityMainBinding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseApplication.getApplicationComponent().inject(this)

        createViewModel()

        initUI(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun initUI(savedInstanceState: Bundle?) {
        mainActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.main_activity)
        if (savedInstanceState == null)
            initNewsListFragment()
    }

    private fun initNewsListFragment() {
        supportFragmentManager.beginTransaction().setCustomAnimations(
            R.anim.sliding_in_left,
            R.anim.sliding_out_right,
            android.R.anim.slide_in_left,
            android.R.anim.slide_out_right
        ).add(mainActivityMainBinding.screenContainer.id, NewsListFragment())
            .addToBackStack(NEWS_FRAGMENT)
            .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStackImmediate()
        } else {
            finish()
        }
    }

    private fun createViewModel() {
        sharedViewModel =
            ViewModelProvider(this, viewmodelFactory)
                .get(MainActivitySharedViewModel::class.java)

        implementObservables()
    }

    private fun implementObservables() {
        sharedViewModel.openDetailsFragment.observe(this, Observer { openDetailsActivity(it) })
    }

    private fun openDetailsActivity(newsEntity: NewsEntity) {
        val myIntent = Intent(this@MainActivity, DetailViewActivity::class.java)
        myIntent.putExtra("news_entity", newsEntity)
        this@MainActivity.startActivity(myIntent)
    }

    companion object {
        const val NEWS_FRAGMENT = "News Fragment"
    }
}
