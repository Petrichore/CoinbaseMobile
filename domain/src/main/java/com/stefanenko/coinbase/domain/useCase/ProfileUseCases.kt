package com.stefanenko.coinbase.domain.useCase

import com.stefanenko.coinbase.domain.entity.Profile
import com.stefanenko.coinbase.domain.entity.ResponseState
import com.stefanenko.coinbase.domain.repository.DataRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileUseCases @Inject constructor(private val dataRepository: DataRepository) {

    suspend fun getProfile(accessToken: String): ResponseState<Profile> {
        return dataRepository.getProfile(accessToken)
    }
}