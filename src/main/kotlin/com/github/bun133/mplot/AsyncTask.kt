package com.github.bun133.mplot

abstract class AsyncTask<R> {
    abstract fun doTask(callBack: (R) -> Unit)
}

/**
 * リスト内のタスクを順番に上から処理していきます
 */
fun List<AsyncTask<*>>.doAll(callBack: () -> Unit) {
    if (isEmpty()) {
        callBack()
    } else if (size == 1) {
        this[0].doTask {
            callBack()
        }
    } else {
        var currentIndex = 0
        var current: AsyncTask<*> = this[0]
        var next: AsyncTask<*>? = this[1]
        fun step() {
            current.doTask {
                currentIndex++
                if (next != null) {
                    current = next!!
                    next = this.getOrNull(currentIndex + 1)
                    step()
                } else {
                    callBack()
                }
            }
        }
    }
}

class InstantAsyncTask<R>(val f: () -> R) : AsyncTask<R>() {
    override fun doTask(callBack: (R) -> Unit) {
        callBack(f())
    }
}

fun <R> instant(body: () -> R): InstantAsyncTask<R> {
    return InstantAsyncTask(body)
}

class WaitUntilAsyncTask<R>(val f: (callBack: (R) -> Unit) -> Unit) : AsyncTask<R>() {
    override fun doTask(callBack: (R) -> Unit) {
        f(callBack)
    }
}

fun <R> waitUntil(f: (callBack: (R) -> Unit) -> Unit): WaitUntilAsyncTask<R> {
    return WaitUntilAsyncTask(f)
}
