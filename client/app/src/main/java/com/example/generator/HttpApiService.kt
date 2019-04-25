package com.example.generator

import com.google.gson.GsonBuilder
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


data class LinkModel(
    val url: String
)

private val gson = GsonBuilder()
    .setLenient()
    .create()

interface HttpApiService {

    @GET("/api/test")
    fun testGet(
        @Query("password") password: String
    ): Observable<LinkModel>

    @POST("/api/test")
    fun testPost(@Body password: String): Observable<LinkModel>

    companion object {
        fun create(id: String): HttpApiService {

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(id)
                .build()

            return retrofit.create(HttpApiService::class.java)
        }
    }

}
