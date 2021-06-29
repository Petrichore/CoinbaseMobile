package com.stefanenko.coinbase.data.util.scheduler

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RxSchedulersProvider: BaseRxSchedulersProvider {
    override fun io(): Scheduler {
        return Schedulers.io()
    }

    override fun computation(): Scheduler {
        return Schedulers.computation()
    }

    override fun androidMainThread(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}