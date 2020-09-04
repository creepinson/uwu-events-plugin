@file:Suppress("SENSELESS_COMPARISON")

package dev.throwouterror.bukkit.uwu.events.command

import dev.throwouterror.bukkit.uwu.events.data.EventManager
import dev.throwouterror.bukkit.uwu.events.util.TextUtils
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project CreepinoUtils
 */
class EventCommand : CommandExecutor {
    // TODO: add permission nodes
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.isEmpty()) {
            // TODO: add help info
            sender.sendMessage(ChatColor.RED.toString() + "Not enough arguments!")
            return false
        }
        if (args.size == 1 && args[0].equals("list", ignoreCase = true)) {
            sender.sendMessage(TextUtils.color("&7&m---- &bEvents List &7&m---"))
            sender.sendMessage(" ")
            EventManager.list().forEach {
                sender.sendMessage("  Id: " + it.id)
                sender.sendMessage("  Name: " + it.name)
                sender.sendMessage("  Active: " + it.enabled)
                sender.sendMessage("  Server Name: " + it.server)
                sender.sendMessage(" -- ")
            }
            sender.sendMessage(" ")
            sender.sendMessage(TextUtils.color("&7&m------"))
        } else if (args.size == 2 && args[0].equals("join", ignoreCase = true) && args[1].isNotEmpty()) {
            if (sender is Player) {
                if (!EventManager.join(args[1].toInt(), arrayListOf(sender)))
                    sender.sendMessage(ChatColor.RED.toString() + "Error joining event " + args[1])
            } else {
                sender.sendMessage(ChatColor.RED.toString() + "You must be a player to use this command!")
            }
        } else
            if (args.size >= 2 && args[0].isNotEmpty() && args[1].isNotEmpty()) {
                if (!sender.hasPermission("uwu.event.admin")) {
                    sender.sendMessage(ChatColor.RED.toString() + "You do not have permission to execute this command.")
                    return false
                }
                if (args.size == 3 && args[0].equals("create", ignoreCase = true) && args[2].isNotEmpty()) {
                    EventManager.create(args[1], args[2])
                    sender.sendMessage(ChatColor.DARK_GREEN.toString() + "The event " + args[1] + " has been created.")
                    return true
                } else if (args[0].equals("stop", ignoreCase = true)) {
                    EventManager.toggle(args[1].toInt(), false)
                    sender.sendMessage(ChatColor.GREEN.toString() + "The event " + args[1] + " is no longer joinable.")
                    return true
                } else if (args[0].equals("start", ignoreCase = true)) {
                    EventManager.toggle(args[1].toInt(), true)
                    sender.sendMessage(ChatColor.GREEN.toString() + "The event " + args[1] + " is now joinable.")
                    return true
                } else if (args[0].equals("remove", ignoreCase = true)) {
                    EventManager.remove(args[1].toInt())
                    sender.sendMessage(ChatColor.DARK_RED.toString() + "The event " + args[1] + " has been removed!")
                    return true
                }
            }
        return false
    }
}