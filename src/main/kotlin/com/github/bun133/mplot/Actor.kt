package com.github.bun133.mplot

import net.kyori.adventure.audience.Audience
import org.bukkit.Location
import org.bukkit.entity.Entity

/**
 * Plotを読みあげる演者のインタフェース
 */
interface Actor {
    fun getEntity(): Entity?
    fun getLocation(): Location?

    /**
     * @return この演者から声が届く範囲にいるAudience
     */
    fun listenerRange(): List<Audience>

    fun doPlot(plot: Plot<*>) {
        plot.forEach {
            doPlotEntry(it)
        }
    }

    fun doPlotEntry(entry: PlotEntry) {
        entry.action().map { doAction(it) }.doAll { }
    }

    fun doAction(action: PlotAction): AsyncTask<*>
}