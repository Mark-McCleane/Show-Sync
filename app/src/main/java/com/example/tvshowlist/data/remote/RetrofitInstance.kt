package com.example.tvshowlist.data.remote

import com.example.tvshowlist.data.remote.RetrofitInterface.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object {
        private val retrofit by lazy {
            val loggingInterceptor = HttpLoggingInterceptor()
            val bearerToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJjY2FhYjc3YWQxNmU2YTgwMGU1MWNmNTljNDZkYTg3NCIsIm5iZiI6MTcyOTU0MjU1NS41NzA0NjIsInN1YiI6IjVjYzg2YzM1YzNhMzY4MjgyMDg1ZjA1MyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.zL6WNyHsxdKZzlpmlcyhcKnnaByNkGcEDJKiml9sDZU"
            val authorizationInterceptor = AuthorizationInterceptor(bearerToken =  bearerToken)
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).addInterceptor(authorizationInterceptor).build()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val api by lazy { retrofit.create(RetrofitInterface::class.java) }
    }
}