package com.stefanenko.coinbase.util.coinbase

import java.lang.StringBuilder

object ScopeBuilder {

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