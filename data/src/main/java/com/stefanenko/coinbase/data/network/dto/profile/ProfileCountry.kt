package com.stefanenko.coinbase.data.network.dto.profile

import com.google.gson.annotations.SerializedName

data class ProfileCountry(
    @SerializedName("code")
    val code: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("is_in_europe")
    val isInEurope: String
)