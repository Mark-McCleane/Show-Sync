package com.example.tvshowlist.data.remote

import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor(private val bearerToken: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder().header("Authorization" , "Bearer $bearerToken")
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}