package com.stefanenko.coinbase.data.network.dto.profile

import com.google.gson.annotations.SerializedName

data class ResponseProfile(
    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("avatar_url")
    val imageUrl: String,

    @SerializedName("country")
    val country: ProfileCountry
)