package dev.throwouterror.bukkit.uwu.events.util

import com.google.common.io.ByteStreams
import dev.throwouterror.bukkit.uwu.events.core.EventPlugin.Companion.instance
import org.bukkit.ChatColor
import org.bukkit.entity.Player

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project CreepinoUtils
 */
object BungeeUtils {
    fun sendPlayerToServer(player: Player, server: String) {
        try {
            val out = ByteStreams.newDataOutput()
            out.writeUTF("Connect")
            out.writeUTF(server)
            player.sendPluginMessage(instance, "BungeeCord", out.toByteArray())
        } catch (e: Exception) {
            player.sendMessage(ChatColor.RED.toString() + "Error when trying to connect to " + server)
        }
    }
}