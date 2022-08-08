package com.zefip.demo7877231

import com.zefip.demo7877231.PostService.Companion.BASE_URL
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
class MainPresenter : MvpPresenter<MainView>() {

    lateinit var compositeDisposable: CompositeDisposable
    lateinit var postInterface: PostService

    private var mPosts = ArrayList<Int>()
    private var mCurrentPage = -1

    override fun onFirstViewAttach() {
        compositeDisposable = CompositeDisposable()

        postInterface = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(PostService::class.java)

        compositeDisposable
            .add(
                postInterface.getPosts().observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::getPostsHandleResponse, this::onError, this::onComplete)
            )
    }

    private fun onComplete() {
        viewState.hideProgressBar()
    }

    private fun getPostsHandleResponse(posts: ArrayList<Int>) {
        mPosts = posts
        nextPost()
    }

    private fun onError(e: Throwable?) {
        if (e is HttpException) {
            viewState.setTextPostError(e.code())
        }
    }

    private fun getPostHandleResponse(post: Post) {
        if (post.type == PostService.POST_TYPE_TEXT) {
            viewState.setTextPost(post.payload.text)
            viewState.showTextPost()
        } else {
            viewState.openWebpage(post.payload.url)
            viewState.showWebpage()
        }
    }

    fun nextPost() {
        if (mCurrentPage + 1 <= mPosts.size - 1 && mCurrentPage + 1 >= 0) {
            mCurrentPage++
            getPostForCurrentPage()
        }
    }

    fun previousPost() {
        if (mCurrentPage - 1 > -1) {
            mCurrentPage--
            getPostForCurrentPage()
        }
    }

    private fun getPostForCurrentPage() {
        compositeDisposable
            .add(
                postInterface.getPostById(mPosts[mCurrentPage]).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::getPostHandleResponse, this::onError, this::onComplete)
            )
    }

    fun compositeDisposableClear() {
        compositeDisposable.clear()
    }
}