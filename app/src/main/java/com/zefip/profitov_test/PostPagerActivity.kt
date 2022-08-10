package com.zefip.profitov_test

import android.os.Bundle
import android.view.View
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.zefip.profitov_test.PostService.Companion.POST_TYPE_TEXT
import com.zefip.profitov_test.databinding.ActivityPostBinding
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter

class PostPagerActivity : MvpAppCompatActivity(), PostPagerView {

    @InjectPresenter
    lateinit var postPagerPresenter: PostPagerPresenter

    private lateinit var viewPager: ViewPager2
    private var _binding: ActivityPostBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initViewPager(posts: ArrayList<Post>) {
        viewPager = binding.pager
        val pagerAdapter = PostPagerAdapter(this.supportFragmentManager)
        for (post in posts) {
            when (post.type) {
                POST_TYPE_TEXT -> pagerAdapter.addFragment(PostTextFragment(post.payload.text))
                else -> pagerAdapter.addFragment(PostWebPageFragment(post.payload.url))
            }
        }
        viewPager.adapter = pagerAdapter
    }

    override fun onError(code: Int) {
        viewPager = binding.pager
        val pagerAdapter = PostPagerAdapter(this.supportFragmentManager)
        pagerAdapter.addFragment(PostTextFragment("${getString(R.string.textview_post_text_error)} $code"))
        viewPager.adapter = pagerAdapter
    }

    override fun showProgressBar() {
        binding.progressbar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        binding.progressbar.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        postPagerPresenter.compositeDisposableClear()
    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            super.onBackPressed()
        } else {
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }

    private inner class PostPagerAdapter(fragmentManager: FragmentManager) : FragmentStateAdapter(fragmentManager, lifecycle) {
        private val fragmentList: ArrayList<Fragment> = ArrayList()

        @NonNull
        override fun createFragment(position: Int): Fragment {
            return fragmentList[position]
        }

        fun addFragment(fragment: Fragment) {
            fragmentList.add(fragment)
        }

        override fun getItemCount(): Int {
            return fragmentList.size
        }
    }
}