package com.stefanenko.coinbase.domain.useCase

import android.net.Uri
import com.stefanenko.coinbase.domain.entity.ResponseState
import com.stefanenko.coinbase.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthUseCases @Inject constructor(private val repository: AuthRepository){

    fun startAuth(): Uri {
        return repository.startAuth()
    }

    suspend fun completeAuth(url: String): ResponseState<Pair<String, String>>{
        return repository.completeAuth(url)
    }

    suspend fun refreshToken(refreshToken: String): ResponseState<Pair<String, String>>{
        return repository.refreshToken(refreshToken)
    }
}