package com.stefanenko.coinbase.data.service.webSocket

import android.util.Log
import com.stefanenko.coinbase.data.util.scheduler.BaseRxSchedulersProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketService @Inject constructor(
    private val rxWebSocketManager: RxWebSocketManager,
    private val schedulersProvider: BaseRxSchedulersProvider
) {

    private val disposables = CompositeDisposable()

    fun startDataStream(
        url: String,
        onNewEvent: (RxWebSocketManager.WebSocketEvent) -> Unit
    ): Disposable {
        val disposable = rxWebSocketManager.start(url)
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.androidMainThread())
            .subscribe({ socketEvent ->
                onNewEvent.invoke(socketEvent)
            }, {
                it.printStackTrace()
                //TODO what kind of error represents in this section??
                //classify error and figure out how to handle it
            }, {
                Log.d("WebSocketEvent:::", "onComplete call")
            })
        disposables.add(disposable)
        return disposable
    }

    fun stopDataStream(disposable: Disposable) {
        rxWebSocketManager.stop()
        deleteDisposable(disposable)
    }

    private fun deleteDisposable(disposable: Disposable) {
        disposables.delete(disposable)
    }
}