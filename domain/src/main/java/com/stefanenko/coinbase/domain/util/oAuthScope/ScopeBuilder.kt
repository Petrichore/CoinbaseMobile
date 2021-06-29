package com.stefanenko.coinbase.domain.util.oAuthScope

import java.lang.StringBuilder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScopeBuilder @Inject constructor() {

    fun build(vararg scopes: String): String {
        if (scopes.isNotEmpty()) {
            val scopeStringBuilder = StringBuilder(scopes[0])

            scopes.forEach { scopeString ->
                scopeStringBuilder.append("+$scopeString")
            }

            return scopeStringBuilder.toString()
        } else {
            return ""
        }
    }
}