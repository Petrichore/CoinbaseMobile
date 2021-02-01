package com.stefanenko.coinbase.data.util.scheduler

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

interface BaseRxSchedulersProvider {
    fun io(): Scheduler
    fun computation(): Scheduler
    fun androidMainThread(): Scheduler
}