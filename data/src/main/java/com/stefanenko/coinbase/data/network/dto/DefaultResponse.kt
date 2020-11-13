package com.stefanenko.coinbase.data.network.dto

import com.google.gson.annotations.SerializedName

data class DefaultResponse<T>(
    @SerializedName("data")
    val data: T
)