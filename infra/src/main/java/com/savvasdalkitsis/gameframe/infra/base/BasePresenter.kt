package com.savvasdalkitsis.gameframe.infra.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BasePresenter<V: BaseView> {

    protected var view: V? = null
    protected val managedStreams = CompositeDisposable()

    fun bindView(view: V) {
        this.view = view
    }

    fun stop() {
        view = null
        clearStreams()
    }

    protected fun clearStreams() {
        managedStreams.clear()
    }

}

operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    add(disposable)
}