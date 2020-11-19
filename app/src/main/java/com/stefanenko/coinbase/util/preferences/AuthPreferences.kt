package com.stefanenko.coinbase.util.preferences

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("PrivatePropertyName")

@Singleton
class AuthPreferences @Inject constructor(private val appContext: Context) {

    private val AUTH_PREF_TAG = "awjdaiwdeuh4ihu328323j221312edscc"

    private val ACCESS_TOKEN_KEY = "ivnru7327398hdwimxwoie8o4923hyguiudscd"
    private val REFRESH_TOKEN_KEY = "ugheorifjo39202103812uwenfnwepg935o835uhdjke"
    private val IS_AUTH_KEY = "ugreo0823423nsfcjvursrvn823989234jnkvkrsjnru"

    fun saveAccessToken(token: String) {
        val sPref = appContext.getSharedPreferences(AUTH_PREF_TAG, Context.MODE_PRIVATE)
        sPref.edit().putString(ACCESS_TOKEN_KEY, token).apply()
    }

    fun getAccessToken(): String {
        val sPref = appContext.getSharedPreferences(AUTH_PREF_TAG, Context.MODE_PRIVATE)
        return sPref.getString(ACCESS_TOKEN_KEY, "") ?: ""
    }

    fun saveRefreshToken(token: String) {
        val sPref = appContext.getSharedPreferences(AUTH_PREF_TAG, Context.MODE_PRIVATE)
        sPref.edit().putString(REFRESH_TOKEN_KEY, token).apply()
    }

    fun getRefreshToken(): String {
        val sPref = appContext.getSharedPreferences(AUTH_PREF_TAG, Context.MODE_PRIVATE)
        return sPref.getString(REFRESH_TOKEN_KEY, "") ?: ""
    }

    fun setUserAuthState(isAuth: Boolean) {
        val sPref = appContext.getSharedPreferences(AUTH_PREF_TAG, Context.MODE_PRIVATE)
        sPref.edit().putBoolean(IS_AUTH_KEY, isAuth).apply()
    }

    fun isUserAuth(): Boolean {
        val sPref = appContext.getSharedPreferences(AUTH_PREF_TAG, Context.MODE_PRIVATE)
        return sPref.getBoolean(IS_AUTH_KEY, false)
    }

    fun clearLoginSate(){
        saveRefreshToken("")
        saveAccessToken("")
        setUserAuthState(false)
    }
}
