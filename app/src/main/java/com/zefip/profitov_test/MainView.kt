package com.zefip.demo7877231

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface MainView : MvpView {

    @AddToEndSingle
    fun showProgressBar()

    @AddToEndSingle
    fun hideProgressBar()

    @AddToEndSingle
    fun showTextPost()

    @AddToEndSingle
    fun showWebpage()

    @AddToEndSingle
    fun setTextPost(text: String)

    @AddToEndSingle
    fun setTextPostError(error: Int)

    @AddToEndSingle
    fun openWebpage(url: String)
}