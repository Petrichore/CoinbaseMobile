package com.stefanenko.coinbase.data.util.scheduler

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class TestRxSchedulersProvider: BaseRxSchedulersProvider {
    override fun io(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun computation(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun androidMainThread(): Scheduler {
        return Schedulers.trampoline()
    }
}