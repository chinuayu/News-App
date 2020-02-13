package news.agoda.com.sample.base

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    private var baseActivity: AppCompatActivity? = null

    override fun onAttach(context: Context) {
        context.let { super.onAttach(it) }
        baseActivity = context as AppCompatActivity?
    }

    override fun onDetach() {
        super.onDetach()
        baseActivity = null
    }
}