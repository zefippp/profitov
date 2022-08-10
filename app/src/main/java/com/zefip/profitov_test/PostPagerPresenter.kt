package com.zefip.profitov_test

import com.zefip.profitov_test.PostService.Companion.BASE_URL
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@InjectViewState
class PostPagerPresenter : MvpPresenter<PostPagerView>() {

    lateinit var compositeDisposable: CompositeDisposable
    lateinit var postInterface: PostService
    private var mPosts = ArrayList<Post>()

    override fun onFirstViewAttach() {
        compositeDisposable = CompositeDisposable()

        postInterface = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(PostService::class.java)

        compositeDisposable
            .add(
                postInterface.getPosts()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        this::getPostsHandleResponse,
                        this::onError,
                        this::getPostsOnComplete
                    )
            )
    }

    private fun getPostsOnComplete() {
        viewState.hideProgressBar()
    }

    private fun getPostsHandleResponse(posts: ArrayList<Int>) {
        getPostForCurrentPage(posts)
    }

    private fun onError(e: Throwable?) {
        if (e is HttpException) {
            e.printStackTrace()
            viewState.onError(e.code())
            viewState.hideProgressBar()
        }
    }
    private fun getPostForCurrentPage(posts: ArrayList<Int>) {
        for (post in posts) {
            compositeDisposable
                .add(
                    postInterface.getPostById(post)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ mPosts.add(it) }, this::onError, this::getPostByIdOnComplete)
                )
        }
    }

    private fun getPostByIdOnComplete() {
        viewState.hideProgressBar()
        viewState.initViewPager(mPosts)
    }

    fun compositeDisposableClear() {
        compositeDisposable.clear()
    }
}