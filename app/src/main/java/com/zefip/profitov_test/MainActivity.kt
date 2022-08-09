package com.zefip.profitov_test

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import com.zefip.profitov_test.databinding.ActivityMainBinding
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter

class MainActivity : MvpAppCompatActivity(), MainView {

    @InjectPresenter
    lateinit var mainPresenter: MainPresenter

    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.btnNextPost.setOnClickListener { mainPresenter.nextPost() }
        binding.btnBackPost.setOnClickListener { mainPresenter.previousPost() }
    }

    override fun showProgressBar() {
        binding.progressbar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        binding.progressbar.visibility = View.GONE
    }

    override fun showTextPost() {
        binding.textviewPostText.visibility = View.VISIBLE
        binding.webviewPostUrl.visibility = View.GONE
    }

    override fun showWebpage() {
        binding.webviewPostUrl.visibility = View.VISIBLE
        binding.textviewPostText.visibility = View.GONE
    }

    override fun setTextPost(text: String) {
        binding.textviewPostText.text = text
    }

    override fun setTextPostError(error: Int) {
        binding.textviewPostText.text = "${getString(R.string.textview_post_text_error)} $error"
    }

    override fun openWebpage(url: String) {
        binding.webviewPostUrl.webViewClient = WebViewClient()
        binding.webviewPostUrl.settings.javaScriptEnabled = true
        binding.webviewPostUrl.loadUrl(url)
    }

    override fun onDestroy() {
        super.onDestroy()
        mainPresenter.compositeDisposableClear()
    }
}
