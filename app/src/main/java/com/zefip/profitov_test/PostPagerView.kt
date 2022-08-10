package com.zefip.profitov_test

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface PostPagerView : MvpView {

    @AddToEndSingle
    fun initViewPager(countPosts: ArrayList<Post>)

    @AddToEndSingle
    fun onError(code: Int)

    @AddToEndSingle
    fun showProgressBar()

    @AddToEndSingle
    fun hideProgressBar()
}