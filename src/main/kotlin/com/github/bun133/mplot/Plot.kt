package com.github.bun133.mplot

/**
 * プロット全体のインタフェース
 */
interface Plot<E : PlotEntry> : Iterable<E> {
    fun forEach(f: (E) -> Unit) = this.iterator().forEach(f)
}

/**
 * プロットの一行に当たる部分
 */
interface PlotEntry {
    /**
     * このプロットの内容
     * セリフやセリフ間の待ちなどもすべて[PlotAction]で表現する
     */
    fun action(): List<PlotAction>
}