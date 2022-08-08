package com.zefip.profitov_test

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindString
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.zefip.demo7877231.MainPresenter
import com.zefip.demo7877231.MainView
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter

class MainActivity : MvpAppCompatActivity(R.layout.activity_main), MainView {

    @InjectPresenter lateinit var mainPresenter: MainPresenter

    @BindView(R.id.textview_post_text) lateinit var textViewPostText: TextView
    @BindView(R.id.webview_post_url) lateinit var webviewPostUrl: WebView
    @BindView(R.id.progressbar) lateinit var progressBar: ProgressBar
    @BindString(R.string.textview_post_text_error) lateinit var textViewPostTextError: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ButterKnife.bind(this)
    }

    @OnClick(R.id.btn_next_post)
    fun onBtnNextPost() {
        mainPresenter.nextPost()
    }

    @OnClick(R.id.btn_back_post)
    fun onBtnPreviousPost() {
        mainPresenter.previousPost()
    }

    override fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    override fun showTextPost() {
        textViewPostText.visibility = View.VISIBLE
        webviewPostUrl.visibility = View.GONE
    }

    override fun showWebpage() {
        webviewPostUrl.visibility = View.VISIBLE
        textViewPostText.visibility = View.GONE
    }

    override fun setTextPost(text: String) {
        textViewPostText.text = text
    }

    override fun setTextPostError(error: Int) {
        textViewPostText.text = "$textViewPostTextError $error"
    }

    override fun openWebpage(url: String) {
        webviewPostUrl.webViewClient = WebViewClient()
        webviewPostUrl.settings.javaScriptEnabled = true
        webviewPostUrl.loadUrl(url)
    }

    override fun onDestroy() {
        super.onDestroy()
        mainPresenter.compositeDisposableClear()
    }
}
