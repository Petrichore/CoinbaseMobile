package com.stefanenko.coinbase.domain.useCase

import com.stefanenko.coinbase.domain.entity.ExchangeRate
import com.stefanenko.coinbase.domain.entity.ResponseState
import com.stefanenko.coinbase.domain.repository.DataRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesUseCases @Inject constructor(private val dataRepository: DataRepository) {

    suspend fun getFavorites(): ResponseState<List<ExchangeRate>> {
        return dataRepository.getFavorites()
    }

    suspend fun addFavorite(exchangeRate: ExchangeRate): ResponseState<Boolean> {
        return dataRepository.addExchangeRateToFavorites(exchangeRate)
    }

    suspend fun deleteFavorite(exchangeRate: ExchangeRate): ResponseState<Boolean> {
        return dataRepository.deleteExchangeRateFromFavorites(exchangeRate)
    }
}