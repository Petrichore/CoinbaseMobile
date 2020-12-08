package com.stefanenko.coinbase.util.deppLink

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeepLinkParser @Inject constructor() {

    fun parse(url: String): String {
        return url.subSequence(BITMEX_TRADE_BASE.length, url.length).toString()
    }
}