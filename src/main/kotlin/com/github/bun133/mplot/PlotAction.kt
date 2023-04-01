package com.github.bun133.mplot

import net.kyori.adventure.text.Component
import org.bukkit.Instrument
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin

/**
 * プロットの前後に存在するアクション
 */
sealed interface PlotAction {
    data class Dialogue(val component: Component) : PlotAction
    data class Pause(val plugin: JavaPlugin, val durationTick: Long) : PlotAction
    data class Walk(val plugin: JavaPlugin,val to: Location) : PlotAction
    data class LocatedSound(val playLocation: Location, val sound: net.kyori.adventure.sound.Sound) : PlotAction
    data class Sound(val sound: net.kyori.adventure.sound.Sound) : PlotAction
    data class LocatedNote(val playLocation: Location, val instrument: Instrument, val note: org.bukkit.Note) :
        PlotAction

    data class Note(val instrument: Instrument, val note: org.bukkit.Note) : PlotAction
    data class Complex(val callBack: (actor: Actor) -> Unit) : PlotAction
}