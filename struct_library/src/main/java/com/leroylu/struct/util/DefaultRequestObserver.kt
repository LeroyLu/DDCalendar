package com.leroylu.struct.util

import com.leroylu.struct.AppException
import com.leroylu.struct.util.net.RequestStatus
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

/**
 * @author jiaj.lu
 * @date 2020/8/21
 * @description
 */
abstract class DefaultRequestObserver<T>(
    private val status: RequestStatus
) : Observer<T> {

    override fun onComplete() {
        status.current.postValue(RequestStatus.Status.DONE)
    }

    override fun onSubscribe(d: Disposable?) {
        status.current.postValue(RequestStatus.Status.DOING)
    }

    override fun onNext(t: T) {
        try {
            process(t)
            onComplete()
        } catch (e: Throwable) {
            onError(e)
        }
    }

    override fun onError(e: Throwable?) {

        status.current.postValue(RequestStatus.Status.ERROR.apply {
            if (e is AppException) error = e
            else {
                e?.printStackTrace()
                ToastUtil.show(e?.message)
            }
        })
    }

    abstract fun process(t: T)
}