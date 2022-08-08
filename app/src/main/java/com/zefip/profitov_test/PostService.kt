package com.zefip.profitov_test

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface PostService {
    @GET("post/{id}")
    fun getPostById(@Path("id") id: Int): Observable<Post>

    @GET("hot")
    fun getPosts(): Observable<ArrayList<Int>>

    companion object {
        const val POST_TYPE_TEXT = "text"
        const val BASE_URL = "http://demo7877231.mockable.io/api/v1/"
    }
}