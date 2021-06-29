package com.stefanenko.coinbase.domain.useCase

import android.net.Uri
import com.stefanenko.coinbase.domain.entity.ResponseState
import com.stefanenko.coinbase.domain.repository.AuthManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthUseCases @Inject constructor(private val manager: AuthManager){

    fun startAuth(): Uri {
        return manager.startAuth()
    }

    suspend fun completeAuth(url: String): ResponseState<Pair<String, String>>{
        return manager.completeAuth(url)
    }

    suspend fun refreshToken(refreshToken: String): ResponseState<Pair<String, String>>{
        return manager.refreshToken(refreshToken)
    }
}