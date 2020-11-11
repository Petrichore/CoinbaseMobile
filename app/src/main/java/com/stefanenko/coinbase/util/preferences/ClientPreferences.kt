package com.stefanenko.coinbase.util.preferences

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("PrivatePropertyName")
@Singleton
class ClientPreferences @Inject constructor(private val appContext: Context) {

    private val CLIENT_ID = "979fbe1bf76735a1179d82729ed941ffdd5331686891c10c12d1b25f24ac5293"
    private val CLIENT_SECRET = "208dfff66f3af0ad840541b2dc41f417ce785f187913acb6837bdb39e338e207"

    private val CLIENT_PREF_TAG = "hgjfke5pekhrgjirojpsir8329y792874wpvti1948019rhuehfueh239813"
    private val CLIENT_SECRET_KEY = "awdjaowidhaouhrytbvgnmbcmnbu2378304198tghjksecnnbhgb"
    private val CLIENT_ID_KEY = "nvrtkubo8ewu9d392ruahud4fi3hi82oruhfwou423ifoitghtugtgge"

    fun saveClientId() {
        val sPref = appContext.getSharedPreferences(CLIENT_PREF_TAG, Context.MODE_PRIVATE)
        sPref.edit().putString(CLIENT_ID_KEY, CLIENT_ID).apply()
    }

    fun saveClientSecret() {
        val sPref = appContext.getSharedPreferences(CLIENT_PREF_TAG, Context.MODE_PRIVATE)
        sPref.edit().putString(CLIENT_SECRET_KEY, CLIENT_SECRET).apply()
    }

    fun getClientId(): String {
        val sPref = appContext.getSharedPreferences(CLIENT_PREF_TAG, Context.MODE_PRIVATE)
        return sPref.getString(CLIENT_ID_KEY, "") ?: ""
    }

    fun getClientSecret(): String {
        val sPref = appContext.getSharedPreferences(CLIENT_PREF_TAG, Context.MODE_PRIVATE)
        return sPref.getString(CLIENT_SECRET_KEY, "") ?: ""
    }
}
