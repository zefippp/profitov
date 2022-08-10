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

    private fun onError(e: Throwable?) {
        if (e is HttpException) {
            e.printStackTrace()
            viewState.onError(e.code())
            viewState.hideProgressBar()
        }
    }

    private fun getPostsHandleResponse(postsIds: ArrayList<Int>) {
        val posts = ArrayList<Post>()
        for (post in postsIds) {
            compositeDisposable
                .add(
                    postInterface.getPostById(post)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                            { posts.add(it) },
                            this::onError,
                            { getPostByIdOnComplete(posts) })
                )
        }
    }

    private fun getPostByIdOnComplete(posts: ArrayList<Post>) {
        viewState.hideProgressBar()
        viewState.initViewPager(posts)
    }

    fun compositeDisposableClear() {
        compositeDisposable.clear()
    }
}