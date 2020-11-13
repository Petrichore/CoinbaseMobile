package com.stefanenko.coinbase.domain.util

import java.lang.StringBuilder

object UrlBuilder {

    private const val PARAMS_MARKER = "?"
    private const val PARAMS_DELIMITER = "&"

    /**
     * Returns configured URL
     * @param baseUrl represents host name
     * @param path represents additional info about endPoint
     * @param params list of key-value pairs, where first - param key, second - param value
     */

    fun buildUrl(baseUrl: String, vararg params: Pair<String, String>, path: String = ""): String {
        val stringBuilder = StringBuilder(baseUrl)

        if (path.isNotEmpty() && path.isNotBlank()) {
            stringBuilder.append(path)
        }

        if (params.isNotEmpty()) {
            stringBuilder.append(PARAMS_MARKER)
                .append("${params[0].first}=")
                .append(params[0].second)

            if (params.size > 1) {
                for (i in 1 until params.size) {
                    stringBuilder.append(PARAMS_DELIMITER)
                        .append("${params[i].first}=")
                        .append(params[i].second)
                }
            }
        }

        return stringBuilder.toString()
    }
}