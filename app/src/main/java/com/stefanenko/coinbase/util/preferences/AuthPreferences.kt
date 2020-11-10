package com.stefanenko.coinbase.util.preferences

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthPreferences @Inject constructor(private val appContext: Context) {

    private val AUTH_PREF_TAG = "awjdaiwdeuh4ihu328323j221312edscc"
    private val AUTH_TOKEN_VALUE_TAG = "ivnru7327398hdwimxwoie8o4923hyguiudscd"
    private val IS_AUTH_VALUE_TAG = "ugreo0823423nsfcjvursrvn823989234jnkvkrsjnru"

    fun saveAuthToken(token: String) {
        val sPref = appContext.getSharedPreferences(AUTH_PREF_TAG, Context.MODE_PRIVATE)
        sPref.edit().putString(AUTH_TOKEN_VALUE_TAG, token).apply()
    }

    fun getAuthToken(): String {
        val sPref = appContext.getSharedPreferences(AUTH_PREF_TAG, Context.MODE_PRIVATE)
        return sPref.getString(AUTH_TOKEN_VALUE_TAG, "") ?: ""
    }

    fun setUserAuthState(isAuth: Boolean) {
        val sPref = appContext.getSharedPreferences(AUTH_PREF_TAG, Context.MODE_PRIVATE)
        sPref.edit().putBoolean(IS_AUTH_VALUE_TAG, isAuth).apply()
    }

    fun isUserAuth(): Boolean {
        val sPref = appContext.getSharedPreferences(AUTH_PREF_TAG, Context.MODE_PRIVATE)
        return sPref.getBoolean(IS_AUTH_VALUE_TAG, false)
    }
}
