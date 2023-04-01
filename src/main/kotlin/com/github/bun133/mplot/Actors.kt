package com.github.bun133.mplot

import net.kyori.adventure.audience.Audience
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

/**
 * ワールド内にEntityとして存在しないタイプの演者(天の声)
 */
abstract class ConsoleActor : Actor {
    override fun getEntity(): Entity? = null
    override fun getLocation(): Location? = null

    abstract override fun listenerRange(): List<Audience>

    override fun doAction(action: PlotAction): AsyncTask<Unit> {
        return when (action) {
            is PlotAction.Dialogue -> {
                instant {
                    listenerRange().forEach {
                        it.sendMessage(action.component)
                    }
                }
            }

            is PlotAction.Pause -> {
                waitUntil {
                    Bukkit.getServer().scheduler.runTaskLater(action.plugin, Runnable { it(Unit) }, action.durationTick)
                }
            }

            is PlotAction.Complex -> {
                instant {
                    action.callBack(this)
                }
            }

            is PlotAction.LocatedNote -> {
                instant {
                    listenerRange().filterIsInstance<Player>().forEach {
                        it.playNote(action.playLocation, action.instrument, action.note)
                    }
                }
            }

            is PlotAction.LocatedSound -> {
                instant {
                    listenerRange().forEach {
                        it.playSound(action.sound, action.playLocation.x, action.playLocation.y, action.playLocation.z)
                    }
                }
            }

            is PlotAction.Note -> throw UnsupportedOperationException()
            is PlotAction.Sound -> {
                instant {
                    listenerRange().forEach {
                        it.playSound(action.sound)
                    }
                }
            }

            is PlotAction.Walk -> throw UnsupportedOperationException()
        }
    }
}

class LivingEntityActor(private val en: LivingEntity, private var listenerRange: Double) : ConsoleActor() {
    init {
        en.isInvulnerable = true
        en.setAI(false)
        // TODO Cancel Damage Event
    }

    override fun getEntity(): LivingEntity = en
    override fun getLocation(): Location = en.location

    override fun listenerRange(): List<Audience> {
        return getLocation().getNearbyPlayers(listenerRange).toList()
    }

    override fun doAction(action: PlotAction): AsyncTask<Unit> {
        return when (action) {
            is PlotAction.Dialogue,
            is PlotAction.Pause,
            is PlotAction.Complex,
            is PlotAction.LocatedNote,
            is PlotAction.LocatedSound,
            is PlotAction.Sound -> {
                super.doAction(action)
            }

            is PlotAction.Note -> {
                instant {
                    listenerRange().filterIsInstance<Player>().forEach {
                        it.playNote(getLocation(), action.instrument, action.note)
                    }
                }
            }

            is PlotAction.Walk -> {
                waitUntil<Unit> {
                    getEntity().walkEntityTo(action.plugin, action.to, 20, 1.0) { b ->
                        assert(b)
                        it(Unit)
                    }
                }
            }
        }
    }
}