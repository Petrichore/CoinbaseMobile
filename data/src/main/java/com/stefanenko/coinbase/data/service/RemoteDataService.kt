package com.stefanenko.coinbase.data.service

import com.stefanenko.coinbase.data.network.RetrofitService
import com.stefanenko.coinbase.data.network.api.AuthApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataService @Inject constructor(private val retrofitService: RetrofitService) {
    private val authApi = retrofitService.createAuthService(AuthApi::class.java)

}