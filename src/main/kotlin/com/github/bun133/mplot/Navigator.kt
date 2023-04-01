package com.github.bun133.mplot

import net.minecraft.core.BlockPosition
import net.minecraft.world.entity.EntityInsentient
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftEntity
import org.bukkit.entity.LivingEntity
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask

fun LivingEntity.walkEntityTo(
    plugin: JavaPlugin,
    to: Location,
    range: Int,
    speed: Double,
    callBack: (success: Boolean) -> Unit
) {
    val handle = (this as CraftEntity).handle
    if (handle is EntityInsentient) {
        val navigator = handle.G()
        val path = navigator.a(BlockPosition.a(to.x, to.y, to.z), range)
        navigator.a(path, speed)

        var task: BukkitTask? = null
        task = plugin.server.scheduler.runTaskTimer(
            plugin, Runnable
            {
                if (navigator.l()) {
                    // Walking End
                    task!!.cancel()
                    callBack(true)
                }
            }, 0L, 1L
        )
    } else {
        callBack(false)
    }
}